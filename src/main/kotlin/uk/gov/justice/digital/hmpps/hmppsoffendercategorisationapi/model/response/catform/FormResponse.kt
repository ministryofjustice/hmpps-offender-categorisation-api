package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
data class FormResponse(
  val ratings: Ratings? = null,
)
