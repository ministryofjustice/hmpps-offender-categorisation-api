package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation

import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import org.hibernate.annotations.Type
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.riskprofiler.OffenderNo
import java.time.ZonedDateTime

@Entity
@IdClass(OffenderNo::class)
@Table(name = "prisoner_risk_profile", schema = "public")
class PrisonerRiskProfileEntity(
  @Id
  @Column(name = "offender_no")
  val offenderNo: String,

  @Type(JsonType::class)
  @Column(columnDefinition = "jsonb", name = "risk_profile")
  val riskProfile: String? = null,

  @Column(name = "calculated_at")
  val calculatedAt: ZonedDateTime? = null,
)
