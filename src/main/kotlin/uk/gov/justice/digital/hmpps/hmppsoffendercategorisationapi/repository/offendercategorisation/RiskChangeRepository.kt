package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.RiskChangeEntity

@Repository
interface RiskChangeRepository : JpaRepository<RiskChangeEntity, Long> {

  fun findByStatusAndOffenderNo(
    status: String,
    offenderNo: String,
  ): RiskChangeEntity?

  fun findByOffenderNoOrderByRaisedDateDesc(
    offenderNo: String,
  ): List<RiskChangeEntity>
}
