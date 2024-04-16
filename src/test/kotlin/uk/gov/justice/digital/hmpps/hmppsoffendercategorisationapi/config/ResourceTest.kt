package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config

import jakarta.transaction.Transactional
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlMergeMode

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql("classpath:repository/reset.sql")
abstract class RepositoryTest
