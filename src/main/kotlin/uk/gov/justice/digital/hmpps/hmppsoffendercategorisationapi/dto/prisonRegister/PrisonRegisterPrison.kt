package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonRegister

data class PrisonRegisterPrison(
  val prisonId: String,
  val prisonName: String,
  val active: Boolean,
)
