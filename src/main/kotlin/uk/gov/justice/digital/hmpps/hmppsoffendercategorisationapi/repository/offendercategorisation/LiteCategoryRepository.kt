package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.LiteCategoryEntity
import java.time.ZonedDateTime

@Repository
interface LiteCategoryRepository : JpaRepository<LiteCategoryEntity, Long> {
  fun findByOffenderNoAndCreatedDateBetweenOrApprovedDateBetweenOrderBySequenceDesc(
    offenderNo: String,
    createdDateFrom: ZonedDateTime? = null,
    createdDateTo: ZonedDateTime? = null,
    approvedDateFrom: ZonedDateTime? = null,
    approvedDateTo: ZonedDateTime? = null,
  ): List<LiteCategoryEntity>?
}
