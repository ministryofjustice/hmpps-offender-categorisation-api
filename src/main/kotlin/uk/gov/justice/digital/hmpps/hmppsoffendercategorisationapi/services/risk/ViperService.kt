package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.risk

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.file.ViperRepository
import java.math.BigDecimal

@Service
class ViperService(
  private val viperRepository: ViperRepository,
) {
  fun getViperScore(prisonerNumber: String): BigDecimal? {
    var viperScore: BigDecimal? = null
    viperRepository.getByKey(prisonerNumber).ifPresent {
      viperScore = it.score
    }
    return viperScore
  }
}
