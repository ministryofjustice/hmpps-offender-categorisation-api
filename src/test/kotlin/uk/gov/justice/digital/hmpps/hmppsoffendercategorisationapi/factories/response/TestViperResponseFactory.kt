package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.response

import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.risk.ViperResponse
import java.math.BigDecimal

class TestViperResponseFactory {
  private var prisonerNumber = "123ABC"
  private var score: BigDecimal? = null
  private var aboveThreshold = false

  fun withPrisonerNumber(prisonerNumber: String): TestViperResponseFactory {
    this.prisonerNumber = prisonerNumber
    return this
  }

  fun withScore(score: BigDecimal?): TestViperResponseFactory {
    this.score = score
    return this
  }

  fun withAboveThreshold(aboveThreshold: Boolean): TestViperResponseFactory {
    this.aboveThreshold = aboveThreshold
    return this
  }

  fun build() = ViperResponse(
    prisonerNumber = this.prisonerNumber,
    score = this.score,
    aboveThreshold = this.aboveThreshold,
  )
}
