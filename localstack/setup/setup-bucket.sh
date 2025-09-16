#!/usr/bin/env bash

export AWS_ACCESS_KEY_ID=anykey
export AWS_SECRET_ACCESS_KEY=anysecret
export AWS_DEFAULT_REGION=eu-west-2

HOSTNAME=${EXTERNAL_HOSTNAME:-"localhost"}

echo 'S3 bucket for files'
aws --endpoint-url=http://${HOSTNAME}:4572 s3 mb s3://testbucket

echo 'Seed viper data'
aws --endpoint-url=http://${HOSTNAME}:4572 s3 cp ${SETUP_FOLDER}/s3/VIPER_2_2024_10_29.csv s3://testbucket/viper/VIPER_2_2024_10_29.csv

echo 'Finished localstack bucket setup'
exit 0
