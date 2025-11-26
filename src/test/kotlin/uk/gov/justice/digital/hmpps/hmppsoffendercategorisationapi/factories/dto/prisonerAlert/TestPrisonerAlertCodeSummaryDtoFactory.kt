package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.dto.prisonerAlert

import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonerAlert.PrisonerAlertCodeSummaryDto
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonerAlert.PrisonerAlertResponseDto.Companion.ALERT_CODE_ESCAPE_RISK

class TestPrisonerAlertCodeSummaryDtoFactory {

  private var alertCode = ALERT_CODE_ESCAPE_RISK
  private var alertDescription = "Test alert description"

  fun withAlertCode(alertCode: String): TestPrisonerAlertCodeSummaryDtoFactory {
    this.alertCode = alertCode
    return this
  }

  fun withAlertDescription(alertDescription: String): TestPrisonerAlertCodeSummaryDtoFactory {
    this.alertDescription = alertDescription
    return this
  }

  fun build(): PrisonerAlertCodeSummaryDto = PrisonerAlertCodeSummaryDto(
    this.alertCode,
    this.alertDescription,
  )
}
