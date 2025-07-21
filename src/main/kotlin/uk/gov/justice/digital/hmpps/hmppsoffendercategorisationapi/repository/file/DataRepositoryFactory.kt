package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.file

import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.file.FileType
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.file.FileType.Companion.byDataSet
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.file.RiskDataSet
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.file.Viper

@Component("dataRepositoryFactory")
class DataRepositoryFactory(
  viperRepository: ViperRepository,
) {
  private val viperRepository: DataRepository<Viper>
  fun <T : RiskDataSet> getRepository(type: Class<T>): DataRepository<Viper> {
    return when (byDataSet(type)) {
      FileType.VIPER -> viperRepository
    }
  }

  fun getRepositories(): List<DataRepository<out RiskDataSet>> {
    return listOf(
      getRepository(Viper::class.java),
    )
  }

  init {
    this.viperRepository = viperRepository
  }
}
