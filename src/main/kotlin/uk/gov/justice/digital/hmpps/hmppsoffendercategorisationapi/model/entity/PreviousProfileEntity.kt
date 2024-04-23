package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime

@Entity
@Table(name = "PREVIOUS_PROFILE")
class PreviousProfileEntity(
  @Id
  val id: Long,

  @Column(name = "OFFENDER_NO")
  val offenderNo: String,

  val escape: String,

  val extremism: String,

  val soc: String,

  val violence: String,

  @Column(name = "EXECUTE_DATE_TIME")
  val executeDateTime: ZonedDateTime? = null,
)
