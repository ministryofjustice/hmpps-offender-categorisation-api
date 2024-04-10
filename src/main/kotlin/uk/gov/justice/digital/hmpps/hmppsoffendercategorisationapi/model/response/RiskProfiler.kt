package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response

import com.fasterxml.jackson.annotation.JsonFormat
import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.Column
import kotlinx.serialization.Serializable
import org.hibernate.annotations.Type
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Violence
import java.time.ZonedDateTime

@Serializable
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

  // datetime
  val executeDateTime: String
)
