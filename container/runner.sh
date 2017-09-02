#!/usr/bin/env bash
# This is needed since only the EC2 instance has access to RDS
# The build also has to be done here in that case...
DB_HOST=mindlevel.cxkevz1h137d.eu-central-1.rds.amazonaws.com &&
JDBC_HOST="jdbc:mysql//"$DB_HOST"/mindlevel" &&
cd /root/mindlevel-backend &&
mysql -uroot -ppassword -h $DB_HOST < mindlevel_schema.sql &&
scala target/scala-2.12/mindlevel-backend-assembly-1.0.jar -Ddb.url=$JDBC_HOST
