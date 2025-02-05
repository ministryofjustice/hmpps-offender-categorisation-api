package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.LiteCategoryEntity

@Repository
interface LiteCategoryRepository : JpaRepository<LiteCategoryEntity, Long> {
  fun findAllByOffenderNoOrderBySequenceDesc(
    offenderNo: String,
  ): List<LiteCategoryEntity>
}
