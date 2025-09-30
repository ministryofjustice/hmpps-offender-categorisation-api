package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.risk

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.file.Viper
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.risk.ViperResponse
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.file.DataRepository
import java.math.BigDecimal

@Service
class ViperService(
  private val viperRepository: DataRepository<Viper>,
) {
  fun getViperData(prisonerNumber: String): ViperResponse {
    val viperScore = this.getViperScore(prisonerNumber)
    var aboveThreshold = false
    if (viperScore != null) {
      aboveThreshold = viperScore > VIPER_THRESHOLD.toBigDecimal()
    }
    return ViperResponse(
      prisonerNumber,
      viperScore,
      aboveThreshold,
    )
  }
  fun getViperScore(prisonerNumber: String): BigDecimal? {
    var viperScore: BigDecimal? = null
    viperRepository.getByKey(prisonerNumber).ifPresent {
      viperScore = it.score
    }
    return viperScore
  }

  companion object {
    const val VIPER_THRESHOLD = 2.0
  }
}
