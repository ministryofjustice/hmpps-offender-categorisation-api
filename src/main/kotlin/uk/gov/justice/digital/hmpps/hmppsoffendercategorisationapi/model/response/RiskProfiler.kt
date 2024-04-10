package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response

import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.Column
import org.hibernate.annotations.Type
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Violence
import java.time.ZonedDateTime

data class RiskProfiler(
  val offenderNo: String,

  /**
   * empty json definition
   */
  @Type(JsonType::class)
  @Column(columnDefinition = "jsonb")
  val escape: String?,

  /**
   * empty json definition
   */
  @Type(JsonType::class)
  @Column(columnDefinition = "jsonb")
  val extremism: String?,

  /**
   * empty json definition
   */
  @Type(JsonType::class)
  @Column(columnDefinition = "jsonb")
  val soc: String?,

  val violence: Violence?,

  val executeDateTime: ZonedDateTime
)
