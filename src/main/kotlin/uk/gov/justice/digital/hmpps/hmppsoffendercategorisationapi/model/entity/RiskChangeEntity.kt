package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity

import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.*
import org.hibernate.annotations.Type
import java.time.ZonedDateTime

@Entity
@Table(name = "risk_change")
class RiskChangeEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = -1,

  @Type(JsonType::class)
  @Column(columnDefinition = "jsonb", name = "old_profile")
  val oldProfile: String? = null,

  @Type(JsonType::class)
  @Column(columnDefinition = "jsonb", name = "new_profile")
  val newProfile: String? = null,

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

  )