package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.risk

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.file.Viper
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.file.DataRepository
import java.math.BigDecimal

@Service
class ViperService(
  private val viperRepository: DataRepository<Viper>,
) {
  fun getViperScore(prisonerNumber: String): BigDecimal? {
    var viperScore: BigDecimal? = null
    viperRepository.getByKey(prisonerNumber).ifPresent {
      viperScore = it.score
    }
    return viperScore
  }
}
