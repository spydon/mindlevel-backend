#!/usr/bin/env bash
## Remember that the release branch is used
## DEPENDENCIES: mysql git sbt docker aws-tools
cd `dirname "$0"` &&
ping -c1 github.com &&
rm -rf ./mindlevel-backend &&
git clone -b release git@github.com:spydon/mindlevel-backend.git &&
cd mindlevel-backend &&
mysql -uroot -ppassword mindlevel < mindlevel_schema.sql &&
sbt assembly &&
cd .. &&
docker build -t mindlevel . &&
docker tag mindlevel:latest 589361660625.dkr.ecr.eu-central-1.amazonaws.com/mindlevel:latest &&
$(aws ecr get-login --region eu-central-1 --no-include-email) &&
docker push 589361660625.dkr.ecr.eu-central-1.amazonaws.com/mindlevel:latest
