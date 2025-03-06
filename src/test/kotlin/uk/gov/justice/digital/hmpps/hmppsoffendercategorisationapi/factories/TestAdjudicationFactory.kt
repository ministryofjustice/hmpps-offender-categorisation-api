package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories

import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.adjudication.Adjudication
import java.time.LocalDateTime

class TestAdjudicationFactory {
  private var prisonerNumber = "ABC123"
  private var status = "ACCEPTED"
  private var createdDateTime = LocalDateTime.now().toString()

  fun withPrisonerNumber(prisonerNumber: String): TestAdjudicationFactory {
    this.prisonerNumber = prisonerNumber
    return this
  }
  fun withStatus(status: String): TestAdjudicationFactory {
    this.status = status
    return this
  }
  fun withCreatedDateTime(createdDateTime: String): TestAdjudicationFactory {
    this.createdDateTime = createdDateTime
    return this
  }

  fun build(): Adjudication = Adjudication(
    prisonerNumber = this.prisonerNumber,
    status = this.status,
    createdDateTime = this.createdDateTime,
  )
}
