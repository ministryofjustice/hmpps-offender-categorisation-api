package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class Soc(
  val nomsId: String? = null,
  val transferToSecurity: Boolean? = null,
  val provisionalCategorisation: String? = null,
  private val riskType: String? = null,
)
