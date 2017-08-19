#!/usr/bin/env bash
docker build -t mindlevel . &&
docker tag mindlevel:latest 589361660625.dkr.ecr.eu-central-1.amazonaws.com/mindlevel:latest &&
$(aws ecr get-login --region eu-central-1) &&
docker push 589361660625.dkr.ecr.eu-central-1.amazonaws.com/mindlevel:latest
