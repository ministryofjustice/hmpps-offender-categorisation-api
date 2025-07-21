package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.file

import com.opencsv.CSVReader
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.file.FileType
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.file.PendingFile

/**
 * Polls the s3 folders viper
 */
@Service
class CsvProcessorService(
  private val dataService: DataService,
  private val fileService: S3FileService,
  @Value("\${s3.path.viper}") private val viperPath: String = "/viper",
) {

  private final suspend fun initialise() {
    log.info("Initialising all schedulers")

    if (!initialised) {
      startViperScheduler()

      initialised = true
    }
  }

  @Scheduled(cron = "\${viper.period}")
  final suspend fun startViperScheduler() {
    initialise()
    log.info("Starting VIPER Scheduler - Checking for csv")
    val file = fileService.getLatestViper2File(viperPath, FileType.VIPER)

    if (file != null) {
      dataService.process(unmarshallCsv(file), FileType.VIPER, file)
    }
  }

  private fun unmarshallCsv(file: PendingFile?): ArrayList<List<String>> {
    val records = ArrayList<List<String>>()
    val csvReader = CSVReader(file!!.data.reader())

    var values: Array<String?>? = csvReader.readNext()
    while (values != null) {
      records.add(values.toList().filterIsInstance<String>())
      values = csvReader.readNext()
    }
    return records
  }

  companion object {
    @JvmStatic
    private var initialised: Boolean = false

    private val log = LoggerFactory.getLogger(CsvProcessorService::class.java)
  }
}
