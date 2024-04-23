package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.PreviousProfileEntity

@Repository
interface PreviousProfileRepository : JpaRepository<PreviousProfileEntity, Long> {
  fun findByOffenderNo(offenderNo: String): PreviousProfileEntity?
}