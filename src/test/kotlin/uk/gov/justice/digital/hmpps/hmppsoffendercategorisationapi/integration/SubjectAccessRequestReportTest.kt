package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.reactive.server.WebTestClient
import uk.gov.justice.digital.hmpps.subjectaccessrequest.SarIntegrationTestHelper
import uk.gov.justice.digital.hmpps.subjectaccessrequest.SarIntegrationTestHelperConfig
import uk.gov.justice.digital.hmpps.subjectaccessrequest.SarReportTest

@AutoConfigureWebTestClient
@Import(SarIntegrationTestHelperConfig::class)
@TestPropertySource(
  properties = [
    "hmpps.test.jwt-helper-enabled=true",
  ],
)
@Sql(
  scripts = ["classpath:repository/reset.sql"],
  executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS,
)
@Sql(
  scripts = ["classpath:repository/subject_access_request_service_data.sql"],
  executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS,
)
class SubjectAccessRequestReportTest : IntegrationTestBase(), SarReportTest {

  @Autowired
  lateinit var sarIntegrationTestHelper: SarIntegrationTestHelper

  override fun getSarHelper(): SarIntegrationTestHelper = sarIntegrationTestHelper

  override fun getWebTestClientInstance(): WebTestClient = webTestClient

  override fun getPrn(): String? = "GXXXX"

  override fun setupTestData() {
    // No-op because test data is loaded using @Sql
  }
}