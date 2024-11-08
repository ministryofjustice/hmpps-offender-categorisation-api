#!/usr/bin/env bash
aws --endpoint-url=http://localhost:4566 sns publish \
    --topic-arn arn:aws:sns:eu-west-2:000000000000:prisoneroffendersearch-topic \
    --message-attributes '{"eventType" : { "DataType":"String", "StringValue":"prisoner-offender-search.prisoner.updated"}}' \
    --message '{"type":"prisoner-offender-search.prisoner.updated","id":"2","contents":"prisoner-offender-search.prisoner.updated_message_contents"}'