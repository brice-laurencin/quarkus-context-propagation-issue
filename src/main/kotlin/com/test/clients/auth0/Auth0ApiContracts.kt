package com.test.clients.auth0

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Auth0TokenRequest(
    @field:JsonProperty("grant_type") val grantType: String? = null,
    @field:JsonProperty("audience") val audience: String? = null,
    @field:JsonProperty("client_id") val clientId: String? = null,
    @field:JsonProperty("client_secret") val clientSecret: String? = null,
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Auth0TokenResponse(
    @field:JsonProperty("access_token") val accessToken: String? = null,
    @field:JsonProperty("token_type") val tokenType: String? = null,
    @field:JsonProperty("scope") val scope: String? = null,
    @field:JsonProperty("expires_in") val expiresIn: Long? = null,
    var expiresAt: LocalDateTime? = null,
)
