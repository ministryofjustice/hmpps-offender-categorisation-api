package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config

import org.springframework.core.ParameterizedTypeReference

inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}
