package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.FormEntity
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
interface FormRepository : JpaRepository<FormEntity, Long> {
  fun findByBookingId(bookingId: Long): FormEntity?
  fun findAllByOffenderNoAndStartDateBetweenOrApprovalDateBetweenOrderBySequenceNoAsc(
    offenderNo: String,
    fromStartDate: LocalDateTime? = null,
    toStartDate: LocalDateTime? = null,
    fromApprovalDate: LocalDate? = null,
    toApprovalDate: LocalDate? = null,
  ): List<FormEntity>

  fun findAllByOffenderNoOrderBySequenceNoAsc(
    offenderNo: String,
  ): List<FormEntity>

  fun findAllByStatusNotIn(notInStatuses: List<String>, pageable: Pageable): List<FormEntity>
}
