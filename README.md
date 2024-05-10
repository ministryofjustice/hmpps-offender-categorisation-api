# hmpps-offender-categorisation-api
[![repo standards badge](https://img.shields.io/badge/dynamic/json?color=blue&style=flat&logo=github&label=MoJ%20Compliant&query=%24.result&url=https%3A%2F%2Foperations-engineering-reports.cloud-platform.service.justice.gov.uk%2Fapi%2Fv1%2Fcompliant_public_repositories%2Fhmpps-offender-categorisation-api)](https://operations-engineering-reports.cloud-platform.service.justice.gov.uk/public-github-repositories.html#hmpps-offender-categorisation-api "Link to report")
[![CircleCI](https://circleci.com/gh/ministryofjustice/hmpps-offender-categorisation-api/tree/main.svg?style=svg)](https://circleci.com/gh/ministryofjustice/hmpps-offender-categorisation-api)
[![Docker Repository on Quay](https://quay.io/repository/hmpps/hmpps-offender-categorisation-api/status "Docker Repository on Quay")](https://quay.io/repository/hmpps/hmpps-offender-categorisation-api)
[![API docs](https://img.shields.io/badge/API_docs_-view-85EA2D.svg?logo=swagger)](https://hmpps-offender-categorisation-api-dev.hmpps.service.justice.gov.uk/webjars/swagger-ui/index.html?configUrl=/v3/api-docs)

This is a Spring Boot application, written in Kotlin, providing data
access to support the [Offender Categorisation application](https://github.com/ministryofjustice/offender-categorisation).

## Setup

Start the postrgres form-builder database and localstack from the offender categorisation project. Create a run configuration and apply the following environment variables:

DB_SERVER=localhost;DB_NAME=form-builder;DB_USER=form-builder;DB_PASS=form-builder 

You may need to create the Risk Profiler schema. Best way would be to run the reset.sql test script to create the schema and the previous_profile database table that is used to gather data for SAR.

Set active profile to local and set the user id an token for api.client.admin.id property

## Target Users

User requires role ROLE_SAR_DATA_ACCESS to access SAR information. Without the role a 401 will be returned

## Rest API

The current service exposes only SAR GET Rest Endpoint. This endpoint is standardised in uk.gov.justice.hmpps.kotlin.sar.HmppsPrisonSubjectAccessRequestService which is part of uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter dependency - see build.gradle.kts). 

# Dev endpoints

Open API specification - https://hmpps-offender-categorisation-api-dev.hmpps.service.justice.gov.uk/swagger-ui/index.html?configUrl=/v3/api-docs

Health - https://hmpps-offender-categorisation-api-dev.hmpps.service.justice.gov.uk/health

SAR Request - https://hmpps-offender-categorisation-api-dev.hmpps.service.justice.gov.uk/subject-access-request?prn=5555

## Automated Tests

All unit and integration tests should run straight out of the box.