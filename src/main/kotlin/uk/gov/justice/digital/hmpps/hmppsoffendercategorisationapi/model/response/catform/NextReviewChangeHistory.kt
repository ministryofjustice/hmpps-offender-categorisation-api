package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
class NextReviewChangeHistory(
  val id: String? = null,

  @JsonProperty("booking_id")
  val bookingId: String? = null,

  // date
  @JsonProperty("next_review_date")
  val nextReviewDate: String? = null,

  val reason: String? = null,

  // datetime
  @JsonProperty("change_date")
  val changeDate: String? = null,
)
