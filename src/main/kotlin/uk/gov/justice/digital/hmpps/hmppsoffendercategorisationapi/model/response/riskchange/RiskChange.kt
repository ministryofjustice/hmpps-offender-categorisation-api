package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class RiskChange(

  val id: String? = null,

  @JsonProperty("old_profile")
  val oldProfile: Profile? = null,

  @JsonProperty("new_profile")
  val newProfile: Profile? = null,

  @JsonProperty("offender_no")
  val offenderNo: String? = null,

  @JsonProperty("prison_id")
  val prisonId: String? = null,

  val status: String? = null,

  // datetime
  @JsonProperty("raised_date")
  val raisedDate: String? = null,
)
