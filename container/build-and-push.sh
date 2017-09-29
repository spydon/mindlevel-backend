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
cd `dirname "$0"` &&
ping -c1 github.com &&
rm -rf ./mindlevel-backend &&
git clone -b release git@github.com:spydon/mindlevel-backend.git &&
cd mindlevel-backend &&
mysql -uroot -ppassword mindlevel < mindlevel_schema.sql &&
sbt assembly &&
cd .. &&
docker build -t mindlevel . &&
docker tag mindlevel:latest $awsRepo &&
$(aws ecr get-login --region eu-central-1 --no-include-email) &&
docker push $awsRepo &&
echo "Successfully pushed container image to AWS" &&
aws ecs list-tasks --cluster mindlevel | \
sed '/\([{}].*\|.*taskArns.*\| *]\)/d' | sed 's/ *"\([^"]*\).*/\1/' | \
while read -r task; do aws ecs stop-task --cluster mindlevel --task $task; done &&
aws ecs run-task --cluster mindlevel --task-definition mindlevel:5 --count 1 &&
echo "Successfully restarted the ECS task"
