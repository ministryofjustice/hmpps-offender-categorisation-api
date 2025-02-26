package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories

import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.Alert
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.ConvictedOffence
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.CurrentIncentive
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.Level
import java.time.LocalDate

class TestPrisonerFactory {
  private var prisonerNumber = "123ABC"
  private var bookingId = 12
  private var status = Prisoner.STATUS_ACTIVE_IN
  private var prisonId = "TEST"
  private var restrictedPatient = false
  private var prisonName = "HMP Test"

  private var releaseDate = LocalDate.now().minusDays(1)
  private var category = ""
  private var currentIncentive = CurrentIncentive(
    level = Level("STD", "Standard"),
  )
  private var sentenceStartDate = LocalDate.now().minusDays(1)
  private var legalStatus = ""
  private var convictedOffences = listOf(
    ConvictedOffence(
      offenceCode = "SX56027",
      offenceDescription = "Indecent assault on woman over 16 years of age",
    ),
  )
  private var alerts: List<Alert>? = null

  fun withPrisonerNumber(prisonerNumber: String): TestPrisonerFactory {
    this.prisonerNumber = prisonerNumber
    return this
  }

  fun withBookingId(bookingId: Int): TestPrisonerFactory {
    this.bookingId = bookingId
    return this
  }

  fun withStatus(status: String): TestPrisonerFactory {
    this.status = status
    return this
  }

  fun withPrisonId(prisonId: String): TestPrisonerFactory {
    this.prisonId = prisonId
    return this
  }

  fun withRestrictedPatient(restrictedPatient: Boolean): TestPrisonerFactory {
    this.restrictedPatient = restrictedPatient
    return this
  }

  fun withPrisonName(prisonName: String): TestPrisonerFactory {
    this.prisonName = prisonName
    return this
  }

  fun withReleaseDate(releaseDate: LocalDate): TestPrisonerFactory {
    this.releaseDate = releaseDate
    return this
  }

  fun withCategory(category: String): TestPrisonerFactory {
    this.category = category
    return this
  }

  fun withCurrentIncentive(currentIncentive: CurrentIncentive): TestPrisonerFactory {
    this.currentIncentive = currentIncentive
    return this
  }

  fun withSentenceStartDate(sentenceStartDate: LocalDate): TestPrisonerFactory {
    this.sentenceStartDate = sentenceStartDate
    return this
  }

  fun withLegalStatus(legalStatus: String): TestPrisonerFactory {
    this.legalStatus = legalStatus
    return this
  }

  fun withConvictedOffencesResponse(convictedOffences: List<ConvictedOffence>): TestPrisonerFactory {
    this.convictedOffences = convictedOffences
    return this
  }

  fun withAlerts(alerts: List<Alert>?): TestPrisonerFactory {
    this.alerts = alerts
    return this
  }

  fun build(): Prisoner = Prisoner(
    prisonerNumber = this.prisonerNumber,
    status = this.status,
    prisonId = prisonId,
    restrictedPatient = this.restrictedPatient,
    prisonName = this.prisonName,
    releaseDate = this.releaseDate,
    category = this.category,
    currentIncentive = this.currentIncentive,
    sentenceStartDate = this.sentenceStartDate,
    legalStatus = this.legalStatus,
    allConvictedOffences = this.convictedOffences,
    alerts = this.alerts,
    bookingId = this.bookingId,
  )
}
