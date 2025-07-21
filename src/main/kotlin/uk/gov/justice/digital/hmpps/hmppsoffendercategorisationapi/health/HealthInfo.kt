package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.health

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.boot.actuate.health.Status
import org.springframework.boot.info.BuildProperties
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.file.RiskDataSet
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.file.DataRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.file.DataRepositoryFactory

/**
 * Adds version data to the /health endpoint. This is called by the UI to display API details
 */
@Component
class HealthInfo(buildProperties: BuildProperties) : HealthIndicator {
  private val version: String = buildProperties.version

  @Autowired
  lateinit var dataRepositoryFactory: DataRepositoryFactory

  override fun health(): Health {
    val allAvailable = dataRepositoryFactory.getRepositories()
      .stream().map { obj: DataRepository<out RiskDataSet> -> obj.dataAvailable() }
      .reduce { accumulator: Boolean, dataAvailable: Boolean -> accumulator && dataAvailable }
    return Health.status(
      if (allAvailable.orElse(false)) Status.UP else Status.OUT_OF_SERVICE,
    )
      .withDetail("version", version).build()
  }
}
