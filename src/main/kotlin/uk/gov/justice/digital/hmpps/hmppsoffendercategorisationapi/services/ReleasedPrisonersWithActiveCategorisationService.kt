import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonerSearchApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.FormEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.FormRepository

class ReleasedPrisonersWithActiveCategorisationService(
  private val formRepository: FormRepository,
  private val prisonerSearchApiClient: PrisonerSearchApiClient,
) {
  fun report() {
    try {
      var index = 0
      do {
        val activeCategorisations = formRepository.findAllByStatusNotIn(listOf(FormEntity.STATUS_APPROVED, FormEntity.STATUS_CANCELLED), PageRequest.of(index, CHUNK_SIZE))
        if (activeCategorisations.isEmpty()) {
          continue
        }
        val activeCategorisationsByPrisonerNumber = activeCategorisations.associateBy { it.offenderNo }
        val prisonerNumbers = activeCategorisationsByPrisonerNumber.keys.toList()
        if (prisonerNumbers.isEmpty()) {
          break
        }
        val prisonersFromPrisonerSearch = prisonerSearchApiClient.findPrisonersByPrisonerNumbers(prisonerNumbers)
        reportIfDiscrepancyInPrisonerNumbers(prisonerNumbers, prisonersFromPrisonerSearch)
        for (prisoner in prisonersFromPrisonerSearch) {
          val prisonerNumber = prisoner.prisonerNumber
          if (prisonerNumber === null) {
            continue
          }
          val activeCategorisation = activeCategorisationsByPrisonerNumber[prisonerNumber]
          if (activeCategorisation === null) {
            throw Exception("Prisoner $prisonerNumber returned from prisoner search but not in original list")
          }
          if (!prisoner.currentlyInPrison) {
            log.info("Prisoner $prisonerNumber has active categorisation of type ${activeCategorisation.catType} but prisoner search shows them to have status ${prisoner.status} and restricted patient value ${prisoner.restrictedPatient}")
          }
        }
        index++
      } while (activeCategorisations.count() >= CHUNK_SIZE)
    } catch (e: Exception) {
      log.error("Nominal Synchronisation failed", e)
    }
  }

  private fun reportIfDiscrepancyInPrisonerNumbers(prisonerNumbersFromDatabase: List<String>, prisoners: List<Prisoner>) {
    val prisonerNumbersFromPrisonerSearch = prisoners.map { prisoner -> prisoner.prisonerNumber }
    if (prisonerNumbersFromPrisonerSearch.count() > prisonerNumbersFromDatabase.count()) {
      log.info("Prisoner search returned more prisoner IDs than requested: ${prisonerNumbersFromPrisonerSearch.filter { prisonerNumber -> !prisonerNumbersFromDatabase.contains(prisonerNumber) }}")
    } else if (prisonerNumbersFromPrisonerSearch.count() < prisonerNumbersFromDatabase.count()) {
      log.info("Prisoner search returned less prisoner IDs than requested: ${prisonerNumbersFromDatabase.filter { prisonerNumber -> !prisonerNumbersFromPrisonerSearch.contains(prisonerNumber) }}")
    }
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
    const val CHUNK_SIZE = 50
  }
}
