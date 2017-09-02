#!/usr/bin/env bash
## Remember that the release branch is used
## DEPENDENCIES: mysql git sbt docker aws-tools
awsRegion=eu-central-1
taskDefinitionFile=task.json
taskDefinitionName=mindlevel
serviceName=mindlevel
awsRepo="589361660625.dkr.ecr.eu-central-1.amazonaws.com/mindlevel:latest"

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
docker push $awsRepo

#echo 'Update task definition...'
#aws ecs register-task-definition --cli-input-json file://$taskDefinitionFile --region $awsRegion > /dev/null
#echo 'Update our service with that last task..'
#aws ecs update-service --service $serviceName --task-definition $taskDefinitionName --region $awsRegion  > /dev/null
