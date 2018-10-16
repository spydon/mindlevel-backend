#!/usr/bin/env bash
## This should be run on the ec2 node since it is the only one with connection to the db

SCHEMA=${2:-mindlevel}
DB_HOST=$1
echo "============================================================="
echo " This will result in a few seconds of downtime for $SCHEMA   "
echo "============================================================="
cd "$(dirname "$0")" &&
mysqldump -uroot -ppassword -h$DB_HOST --complete-insert --no-create-info $SCHEMA > ${SCHEMA}_backup.sql &&
[ -s ${SCHEMA}_backup.sql ] &&
mysqladmin -uroot -ppassword -h$DB_HOST -f drop $SCHEMA &&
mysql -uroot -ppassword -h$DB_HOST < ${SCHEMA}_schema.sql &&
mysql -uroot -ppassword -h$DB_HOST $SCHEMA < backup.sql &&
echo "Successfully moved to new schema" || echo "Something failed"
