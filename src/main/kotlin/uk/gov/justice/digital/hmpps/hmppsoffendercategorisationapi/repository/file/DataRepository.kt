package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.file

import org.slf4j.LoggerFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.file.ImportedFile
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.file.RiskDataSet
import java.time.LocalDateTime
import java.util.Optional
import java.util.concurrent.atomic.AtomicBoolean
import java.util.regex.Pattern

abstract class DataRepository<F : RiskDataSet> {
  @JvmField
  val nomisIdRegex = Pattern.compile("^[A-Z]\\d{4}[A-Z]{2}$")
  internal open val dataA = ImportedFile<F>()
  internal open val dataB = ImportedFile<F>()
  internal open val isA = AtomicBoolean(true)
  internal open val dataAvailable = AtomicBoolean(false)
  fun process(csvData: List<List<String>>, filename: String, timestamp: LocalDateTime) {
    doProcess(csvData, filename, timestamp, standbyData)
    toggleData()
    dataAvailable.set(true)
  }

  protected abstract fun doProcess(
    csvData: List<List<String>>,
    filename: String,
    timestamp: LocalDateTime,
    data: ImportedFile<F>,
  )

  open fun getByKey(key: String?): Optional<F> {
    if (data.dataSet != null) {
      return Optional.ofNullable(data.dataSet!![key!!])
    } else {
      return Optional.empty()
    }
  }

  val fileTimestamp: LocalDateTime?
    get() = (if (isA.get()) dataA else dataB).fileTimestamp
  val data: ImportedFile<F>
    get() = if (isA.get()) dataA else dataB
  val standbyData: ImportedFile<F>
    get() = if (isA.get()) dataB else dataA

  private fun toggleData() {
    isA.set(!isA.get())
    log.debug("Switched to {} data map {}", data.fileType, if (isA.get()) "A" else "B")
  }

  fun dataAvailable(): Boolean = dataAvailable.get()

  companion object {
    private val log = LoggerFactory.getLogger(DataRepository::class.java)
  }
}
