package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.risk

import uk.gov.justice.digital.hmpps.riskprofiler.dto.prisonerAlert.PrisonerAlertResponseDto
import java.time.Clock
import java.time.ZonedDateTime

data class EscapeAlert(
  val dateCreated: String,
  val expired: Boolean,
  val active: Boolean,
) {
  companion object {
    fun mapFromDto(dto: PrisonerAlertResponseDto, clock: Clock): EscapeAlert {
      return EscapeAlert(
        dateCreated = dto.createdAt.toString(),
        expired = dto.activeTo?.isBefore(ZonedDateTime.now(clock).toLocalDate()) ?: false,
        active = dto.active,
      )
    }
  }
}
