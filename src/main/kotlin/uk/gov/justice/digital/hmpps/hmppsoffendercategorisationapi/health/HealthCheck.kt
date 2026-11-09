package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.health

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.hmpps.kotlin.health.HealthPingCheck

@Component
class PrisonRegisterApiHealth
constructor(
  @Qualifier("prisonRegisterApiHealthWebClient") webClient: WebClient,
) : HealthPingCheck(webClient)

@Component
class PrisonerSearchApiHealth
constructor(
  @Qualifier("prisonerSearchApiHealthWebClient") webClient: WebClient,
) : HealthPingCheck(webClient)

@Component
class AuthApiHealth
constructor(
  @Qualifier("oauthApiHealthWebClient") webClient: WebClient,
) : HealthPingCheck(webClient)

@Component
class PrisonerAlertsApiHealth
constructor(
  @Qualifier("prisonerAlertsApiHealthWebClient") webClient: WebClient,
) : HealthPingCheck(webClient)

@Component
class PrisonApiHealth
constructor(
  @Qualifier("prisonApiHealthWebClient") webClient: WebClient,
) : HealthPingCheck(webClient)

@Component
class IncidentApiHealth
constructor(
  @Qualifier("incidentApiHealthWebClient") webClient: WebClient,
) : HealthPingCheck(webClient)

@Component
class ManageAdjudicationsApiHealth
constructor(
  @Qualifier("manageAdjudicationsApiHealthWebClient") webClient: WebClient,
) : HealthPingCheck(webClient)

@Component
class AssessRisksAndNeedsApiHealth
constructor(
  @Qualifier("assessRisksAndNeedsApiHealthWebClient") webClient: WebClient,
) : HealthPingCheck(webClient)

@Component
class ManageOffenceApiHealth
constructor(
  @Qualifier("manageOffenceApiHealthWebClient") webClient: WebClient,
) : HealthPingCheck(webClient)

@Component
class ProbationSearchApiHealth
constructor(
  @Qualifier("probationSearchApiHealthWebClient") webClient: WebClient,
) : HealthPingCheck(webClient)
