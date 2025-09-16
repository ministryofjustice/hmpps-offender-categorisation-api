package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.file

import java.time.LocalDateTime

data class PendingFile(
  val fileName: String? = null,
  val fileTimestamp: LocalDateTime? = null,
  val data: String,
)
