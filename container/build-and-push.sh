#!/usr/bin/env bash
## Remember that the release branch is used
## DEPENDENCIES: mysql git sbt docker aws-tools
awsRegion=eu-central-1
taskDefinitionFile=task.json
taskDefinitionName=mindlevel
serviceName=mindlevel
awsRepo="589361660625.dkr.ecr.eu-central-1.amazonaws.com/mindlevel:latest"

echo "==========================================" &&
echo " Remember that the RELEASE branch is used " &&
echo "==========================================" &&
sleep 4 &&
command -v systemctl >/dev/null 2>/dev/null && sudo systemctl start docker &&
echo "Started docker"

cd `dirname "$0"` &&
ping -c1 github.com > /dev/null &&
echo "Got an internet connection" &&
rm -rf ./mindlevel-backend &&
git clone -b release git@github.com:spydon/mindlevel-backend.git > /dev/null &&
echo "Cloned the release branch of the project" &&
cd mindlevel-backend &&
echo "Test local database migration" &&
sh database/upgrade.sh localhost mindlevel &&
sh database/upgrade.sh localhost custom &&
echo "Build fat jar" &&
sbt assembly > /dev/null &&
cd .. &&
echo "Build docker image" &&
docker build -t mindlevel . > /dev/null &&
docker tag mindlevel:latest $awsRepo > /dev/null &&
$(aws ecr get-login --region eu-central-1 --no-include-email) &&
docker push $awsRepo &&
echo "Successfully pushed container image to AWS" &&
aws ecs list-tasks --cluster mindlevel | \
sed '/\([{}].*\|.*taskArns.*\| *]\)/d' | sed 's/ *"\([^"]*\).*/\1/' | \
while read -r task; do aws ecs stop-task --cluster mindlevel --task $task; done > /dev/null &&
aws ecs run-task --cluster mindlevel --task-definition mindlevel --count 1 > /dev/null &&
# Have to deregister and register with the load balancers target group
TARGET_ID=$(aws ec2 describe-instances | python2 -c 'import sys, json; print json.load(sys.stdin)["Reservations"][0]["Instances"][0]["InstanceId"]') &&
aws elbv2 register-targets --target-group-arn arn:aws:elasticloadbalancing:eu-central-1:589361660625:targetgroup/ecs-target-group/a650f52dfd7ae760 --targets Id=$TARGET_ID &&
rm -rf ./mindlevel-backend &&
echo "Successfully restarted the ECS task" || echo "Something failed"
