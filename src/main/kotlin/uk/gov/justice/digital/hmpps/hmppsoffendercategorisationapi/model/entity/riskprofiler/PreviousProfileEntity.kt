package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.riskprofiler

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.io.Serializable
import java.time.ZonedDateTime

/**
 *  Hacking aroudn the old school data model
 */
data class OffenderNo(
  val offenderNo: String = "",
) : Serializable

@Entity
@IdClass(OffenderNo::class)
@Table(name = "PREVIOUS_PROFILE", schema = "risk_profiler")
class PreviousProfileEntity(

  @Id
  @Column(name = "OFFENDER_NO")
  val offenderNo: String,

  val escape: String,

  val extremism: String,

  val soc: String,

  val violence: String,

  @Column(name = "EXECUTE_DATE_TIME")
  val executeDateTime: ZonedDateTime? = null,
)
