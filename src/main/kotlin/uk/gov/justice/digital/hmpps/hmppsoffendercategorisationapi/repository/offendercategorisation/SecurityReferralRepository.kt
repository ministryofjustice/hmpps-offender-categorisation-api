package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.SecurityReferralEntity
import java.time.ZonedDateTime

@Repository
interface SecurityReferralRepository : JpaRepository<SecurityReferralEntity, Long> {
  fun findByOffenderNoAndRaisedDateBetweenOrderByRaisedDateDesc(
    offenderNo: String,
    dateFrom: ZonedDateTime? = null,
    dateTo: ZonedDateTime? = null,
  ): List<SecurityReferralEntity>?
}
