package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation

import jakarta.persistence.*
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.SecurityReferralStatus
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

  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType::class)
  val status: SecurityReferralStatus,

  @Column(name = "raised_date")
  val raisedDate: ZonedDateTime? = null,

  @Column(name = "processed_date")
  val processedDate: ZonedDateTime? = null,
)
