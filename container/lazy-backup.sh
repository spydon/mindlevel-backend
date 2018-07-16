#!/usr/bin/env bash
echo "=====================================================" &&
echo " Remember that the backup is local and needs copying " &&
echo "=====================================================" &&
# Doesn't matter that the password is in clear text since we are in the VPC
mysqldump -u root -ppassword -h mindlevel.cxkevz1h137d.eu-central-1.rds.amazonaws.com mindlevel > mindlevel-backup.sql &&
echo "Successfully wrote backup file" || echo "Something went wrong"
