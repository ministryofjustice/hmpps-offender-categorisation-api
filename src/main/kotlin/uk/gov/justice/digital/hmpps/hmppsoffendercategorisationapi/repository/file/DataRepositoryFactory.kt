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

  private var viperRepository: DataRepository<Viper>? = null

  init {
    this.viperRepository = viperRepository
  }

  fun <T : RiskDataSet> getRepository(type: Class<T>): DataRepository<Viper> = when (byDataSet(type)) {
    FileType.VIPER -> viperRepository ?: throw Exception()
  }

  fun getRepositories(): List<DataRepository<out RiskDataSet>> = listOf(
    getRepository(Viper::class.java),
  )
}
