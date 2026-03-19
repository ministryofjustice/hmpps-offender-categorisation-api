package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime

@Entity
@Table(name = "risk_change", schema = "public")
class RiskChangeEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0,

  @Column(columnDefinition = "jsonb", name = "old_profile")
  val oldProfile: String? = null,

  @Column(columnDefinition = "jsonb", name = "new_profile")
  var newProfile: String? = null,

  @Column(name = "offender_no")
  val offenderNo: String,

  /**
   * REDACTED
   */
  @Column(name = "user_id")
  val userId: String = "",

  @Column(name = "prison_id")
  val prisonId: String,

  val status: String,

  @Column(name = "raised_date")
  var raisedDate: ZonedDateTime? = null,
) {
  companion object {
    const val STATUS_REVIEW_REQUIRED = "REVIEW_REQUIRED"
    const val STATUS_REVIEWED_FIRST = "REVIEWED_FIRST"
    const val STATUS_NEW = "NEW"
    const val STATUS_REVIEW_NOT_REQUIRED = "REVIEW_NOT_REQUIRED"
  }
}
