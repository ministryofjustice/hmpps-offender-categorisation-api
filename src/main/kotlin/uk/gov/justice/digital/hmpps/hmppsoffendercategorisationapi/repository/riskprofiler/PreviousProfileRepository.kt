package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.riskprofiler

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.riskprofiler.PreviousProfileEntity

@Repository
interface PreviousProfileRepository : CrudRepository<PreviousProfileEntity, String> {
  fun findByOffenderNo(offenderNo: String): PreviousProfileEntity?
}
