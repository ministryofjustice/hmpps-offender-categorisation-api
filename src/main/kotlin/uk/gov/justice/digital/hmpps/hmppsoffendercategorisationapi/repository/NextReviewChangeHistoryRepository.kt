package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.NextReviewChangeHistoryEntity

@Repository
interface NextReviewChangeHistoryRepository : JpaRepository<NextReviewChangeHistoryEntity, Long> {
}