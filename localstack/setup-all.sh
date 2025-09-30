#!/usr/bin/env bash

echo 'Setup localstack'
/docker-entrypoint-initaws.d/setup/setup-bucket.sh
echo 'Localstack setup complete'
