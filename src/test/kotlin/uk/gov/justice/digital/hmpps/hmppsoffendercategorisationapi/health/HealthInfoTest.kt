package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.health

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.health.contributor.Status
import org.springframework.boot.info.BuildProperties
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.test.util.ReflectionTestUtils
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.file.DataRepositoryFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.file.ViperRepository
import java.util.Properties
import java.util.concurrent.atomic.AtomicBoolean

@ExtendWith(MockitoExtension::class)
@AutoConfigureWebTestClient
class HealthInfoTest {
  @Mock
  private val buildProperties: BuildProperties? = null

  @Mock
  private val dataRepositoryFactory: DataRepositoryFactory? = null

  private lateinit var healthInfo: HealthInfo
  private lateinit var viperAvailable: AtomicBoolean

  @BeforeEach
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

  @Test
  fun testHealthUp() {
    viperAvailable.set(true)
    val health = healthInfo.health()
    assertThat(health.status).isEqualTo(Status.UP)
    assertThat(health.details).extracting("version").isEqualTo("1.2.3")
  }

  @Test
  fun testHealthOutOfService() {
    viperAvailable.set(false)
    val health = healthInfo.health()
    assertThat(health.status).isEqualTo(Status.OUT_OF_SERVICE)
  }
}
