package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange

import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.Column
import org.hibernate.annotations.Type

data class Profile(
  /**
   * empty json definition
   */
  @Type(JsonType::class)
  @Column(columnDefinition = "jsonb")
  val soc: String?,

  val escape: Escape?,

  val violence: Violence?,

  /**
   * empty json definition
   */
  @Type(JsonType::class)
  @Column(columnDefinition = "jsonb")
  val extremism: String?
)
