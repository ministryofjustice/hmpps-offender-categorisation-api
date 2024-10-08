package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.NextReviewChangeHistoryEntity

@Repository
interface NextReviewChangeHistoryRepository : JpaRepository<NextReviewChangeHistoryEntity, Long> {
  fun findByOffenderNo(offenderNo: String): List<NextReviewChangeHistoryEntity>?
}
