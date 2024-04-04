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

Set active profile to local and set the user id an token for api.client.admin.id property