package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.RiskChangeEntity
import java.time.ZonedDateTime

@Repository
interface RiskChangeRepository : JpaRepository<RiskChangeEntity, Long> {
  fun findByOffenderNoAndRaisedDateBetweenOrderByRaisedDateDesc(
    offenderNo: String,
    fromDate: ZonedDateTime? = null,
    toDate: ZonedDateTime? = null,
  ): List<RiskChangeEntity>?
}
