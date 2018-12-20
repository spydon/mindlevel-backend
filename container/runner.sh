#!/usr/bin/env bash
# This is needed since only the EC2 instance has access to RDS
DB_HOST=mindlevel.cxkevz1h137d.eu-central-1.rds.amazonaws.com
DEFAULT_DB="jdbc:mysql://"$DB_HOST"/mindlevel"
CUSTOM_DB="jdbc:mysql://"$DB_HOST"/custom"
VEGLEVEL_DB="jdbc:mysql://"$DB_HOST"/veglevel"

cd /root/mindlevel-backend &&
sh database/upgrade.sh $DB_HOST mindlevel &&
sh database/upgrade.sh $DB_HOST custom &&
sh database/upgrade.sh $DB_HOST veglevel &&
# TODO: Fix proper logging
scala target/scala-2.12/mindlevel-backend-assembly-1.0.jar -Ddb.default.url=$DEFAULT_DB -Ddb.custom.url=$CUSTOM_DB -Ddb.veglevel.url=$VEGLEVEL_DB &> mindlevel.log
