package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime

/**
 * security referral table
 */
@Entity
@Table(name = "security_referral", schema = "public")
class SecurityReferralEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0,

  @Column(name = "offender_no")
  val offenderNo: String,

  /**
   * REDACTED
   */
  @Column(name = "user_id")
  val userId: String = "",

  @Column(name = "prison_id")
  val prisonId: String,

  /*
  Security referral status enum
   */
  val status: String,

  @Column(name = "raised_date")
  val raisedDate: ZonedDateTime? = null,

  @Column(name = "processed_date")
  val processedDate: ZonedDateTime? = null,
)
