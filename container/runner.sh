#!/usr/bin/env bash
# This is needed since only the EC2 instance has access to RDS
# The build also has to be done here in that case...
cd /root/mindlevel-backend &&
mysql -uroot -ppassword -h mindlevel.cxkevz1h137d.eu-central-1.rds.amazonaws.com < mindlevel_schema.sql &&
scala target/scala-2.12/mindlevel-backend-assembly-1.0.jar
