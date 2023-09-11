package com.test

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import jakarta.ws.rs.core.*
import jakarta.ws.rs.core.MediaType.*

class Auth0ServiceResource : QuarkusTestResourceLifecycleManager {

    companion object {
        const val AUTH0_WIRE_MOCK = "Auth0WireMock"
    }

    private val wireMockServer: WireMockServer by lazy {
        WireMockServer(WireMockConfiguration().dynamicPort().notifier(ConsoleNotifier(true)))
    }

    override fun start(): Map<String, String> {
        wireMockServer.start()

        // It's sad the stubs are setup here, it should be done in the tests, injecting this instance
        wireMockServer.stubFor(
            post(urlEqualTo("/oauth/token"))
                .willReturn(
                    aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                        .withBody("""
{
    "access_token": "randomtoken",
    "token_type": "Bearer",
    "scope": "scope",
    "expires_in": 86400
}
                        """.trimIndent()),
                ),
        )

        return mapOf(
            "auth0-api/mp-rest/url" to wireMockServer.baseUrl(),
        )
    }

    override fun stop() {
        wireMockServer.start()
    }
}
