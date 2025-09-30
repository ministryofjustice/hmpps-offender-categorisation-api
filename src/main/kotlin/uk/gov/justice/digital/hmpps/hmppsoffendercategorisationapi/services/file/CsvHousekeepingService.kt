package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.file

import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class CsvHousekeepingService(
  private val fileService: S3FileService,
  @Value("\${s3.path.viper}") private val viperPath: String = "/viper",
) {

  @Scheduled(fixedRate = DateUtils.MILLIS_PER_DAY)
  suspend fun cleanupHistoricalCsvFiles() {
    fileService.deleteHistoricalFiles(viperPath)
  }
}
