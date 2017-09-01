#!/usr/bin/env bash
## Remember that the release branch is used
git clone -b release git@github.com:spydon/mindlevel-backend.git &&
mysql -uroot -ppassword mindlevel < mindlevel-backend/mindlevel_schema.sql &&
docker build -t mindlevel . &&
docker tag mindlevel:latest 589361660625.dkr.ecr.eu-central-1.amazonaws.com/mindlevel:latest &&
$(aws ecr get-login --region eu-central-1) &&
docker push 589361660625.dkr.ecr.eu-central-1.amazonaws.com/mindlevel:latest
