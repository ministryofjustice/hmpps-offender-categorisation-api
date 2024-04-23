package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity

import com.vladmihalcea.hibernate.type.json.JsonType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Type
import java.time.LocalDateTime

@Entity
@Table(name = "PREVIOUS_PROFILE")
data class PreviousProfile(

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "OFFENDER_NO")
  val offenderNo: String,

  @Type(JsonType::class)
  @Column(columnDefinition = "jsonb", name = "ESCAPE")
  var escape: String,

  @Type(JsonType::class)
  @Column(columnDefinition = "jsonb", name = "SOC")
  var soc: String,

  @Type(JsonType::class)
  @Column(columnDefinition = "jsonb", name = "VIOLENCE")
  var violence: String,

  @Type(JsonType::class)
  @Column(columnDefinition = "jsonb", name = "EXTREMISM")
  var extremism: String,

  @Column(name = "EXECUTE_DATE_TIME")
  var executeDateTime: LocalDateTime,
)
