package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.file

import java.util.Arrays

enum class FileType(val type: Class<out RiskDataSet?>) {
  VIPER(Viper::class.java),
  ;

  companion object {
    @JvmStatic
    fun byDataSet(clazz: Class<out RiskDataSet?>): FileType {
      return Arrays.stream(entries.toTypedArray())
        .filter { ft: FileType -> ft.type == clazz }
        .findFirst().orElse(null)
    }
  }
}
