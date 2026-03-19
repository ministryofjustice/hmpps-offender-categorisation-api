package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.health

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.health.contributor.Status
import org.springframework.boot.info.BuildProperties
import java.util.Properties

class HealthInfoTest {

  private lateinit var healthInfo: HealthInfo

  @BeforeEach
  fun setup() {
    val properties = Properties().apply { setProperty("version", "1.2.3") }
    healthInfo = HealthInfo(BuildProperties(properties))
  }

  @Test
  fun testHealthIncludesVersion() {
    val health = healthInfo.health()
    assertThat(health.status).isEqualTo(Status.UP)
    assertThat(health.details["version"]).isEqualTo("1.2.3")
  }
}
