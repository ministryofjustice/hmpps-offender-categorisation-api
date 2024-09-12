package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
class SecurityReferral(
  val id: String? = null,

  @JsonProperty("prison_id")
  val prisonId: String?,

  val status: String?,

  @JsonProperty("raised_date")
  val raisedDate: String?,

  @JsonProperty("processed_date")
  val processedDate: String,
)
