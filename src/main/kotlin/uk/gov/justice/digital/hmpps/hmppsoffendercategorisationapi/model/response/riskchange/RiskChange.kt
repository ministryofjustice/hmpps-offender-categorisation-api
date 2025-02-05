package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.RiskChangeEntity

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

  private val status: String? = null,

  // datetime
  @JsonProperty("raised_date")
  val raisedDate: String? = null,
) {
  val riskChangeStatus: String?
    get() = if (this.status == null) {
      null
    } else {
      when (this.status) {
        RiskChangeEntity.STATUS_REVIEW_REQUIRED -> "Review required"
        RiskChangeEntity.STATUS_REVIEWED_FIRST -> "Review took place before risk alert processed"
        RiskChangeEntity.STATUS_NEW -> "New risk change alert"
        RiskChangeEntity.STATUS_REVIEW_NOT_REQUIRED -> "Review not required"
        else -> this.status
      }
    }
}
