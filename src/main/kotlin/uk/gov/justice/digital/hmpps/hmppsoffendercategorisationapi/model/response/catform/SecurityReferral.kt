package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.SecurityReferralStatus

@JsonInclude(JsonInclude.Include.NON_NULL)
class SecurityReferral(
  val id: String? = null,

  @JsonProperty("offender_no")
  val offenderNo: String,

  val prisonId: String?,

  private val statusId: SecurityReferralStatus,

  @JsonProperty("raised_date")
  val raisedDate: String?,

  @JsonProperty("processed_date")
  val processedDate: String,
) {
  val status: String
    get() = when (this.statusId) {
      SecurityReferralStatus.CANCELLED -> "Cancelled"
      SecurityReferralStatus.NEW -> "New"
      SecurityReferralStatus.REFERRED -> "Referred"
      SecurityReferralStatus.COMPLETED -> "Completed"
    }
}
