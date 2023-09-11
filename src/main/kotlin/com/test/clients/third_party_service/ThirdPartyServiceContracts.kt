package com.test.clients.third_party_service

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

// ============================================================================
// Admin profile
// ============================================================================

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class ServiceResponse(
    @field:JsonProperty("id") val id: UUID? = null,
    @field:JsonProperty("name") val nickname: String? = null,
)
