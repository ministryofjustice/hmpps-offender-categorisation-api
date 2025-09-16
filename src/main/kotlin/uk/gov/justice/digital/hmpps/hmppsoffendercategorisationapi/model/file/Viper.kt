package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.file

import java.math.BigDecimal

data class Viper(private val nomisId: String) : RiskDataSet {
  override fun getKey() = nomisId
  var score: BigDecimal? = null

  companion object {
    @JvmField
    var recordId = 0

    @JvmField
    var nomisIdPosition = 0

    @JvmField
    var scorePosition = 2
  }
}
