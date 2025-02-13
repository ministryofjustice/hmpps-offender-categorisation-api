package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner

class Alert(
  val alertCode: String,
  private val active: Boolean,
  private val expired: Boolean,
) {
  val isActiveAndNonExpired: Boolean
    get() = this.active && !this.expired

  companion object {
    const val ESCAPE_RISK_ALERT_CODE = "XER"
    const val ESCAPE_LIST_ALERT_CODE = "XEL"
    const val ESCAPE_LIST_HEIGHTENED_ALERT_CODE = "XELH"
  }
}
