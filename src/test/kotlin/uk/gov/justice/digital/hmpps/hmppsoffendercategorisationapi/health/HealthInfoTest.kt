package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.health

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.boot.actuate.health.Status
import org.springframework.boot.info.BuildProperties
import org.springframework.test.util.ReflectionTestUtils
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.file.DataRepositoryFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.file.ViperRepository
import java.util.Properties
import java.util.concurrent.atomic.AtomicBoolean

@RunWith(MockitoJUnitRunner::class)
class HealthInfoTest {
  @Mock
  private val buildProperties: BuildProperties? = null

  @Mock
  private val dataRepositoryFactory: DataRepositoryFactory? = null

  private lateinit var healthInfo: HealthInfo
  private lateinit var viperAvailable: AtomicBoolean

  @Before
  fun setup() {
    val properties = Properties().apply { setProperty("version", "somever") }
    healthInfo = HealthInfo(BuildProperties(properties))
    ReflectionTestUtils.setField(healthInfo, "buildProperties", buildProperties)
    ReflectionTestUtils.setField(healthInfo, "dataRepositoryFactory", dataRepositoryFactory)
    val viperRepository = ViperRepository()
    viperAvailable =
      ReflectionTestUtils.getField(viperRepository, ViperRepository::class.java, "dataAvailable") as AtomicBoolean

    Mockito.`when`(dataRepositoryFactory!!.getRepositories())
      .thenReturn(listOf(viperRepository))
    Mockito.`when`(buildProperties!!.version).thenReturn("1.2.3")
  }

  @org.junit.Test
  fun testHealthUp() {
    viperAvailable.set(true)
    val health = healthInfo.health()
    assertThat(health.status).isEqualTo(Status.UP)
    assertThat(health.details).extracting("version").isEqualTo("1.2.3")
  }

  @org.junit.Test
  fun testHealthOutOfService() {
    viperAvailable.set(false)
    val health = healthInfo.health()
    assertThat(health.status).isEqualTo(Status.OUT_OF_SERVICE)
  }
}
