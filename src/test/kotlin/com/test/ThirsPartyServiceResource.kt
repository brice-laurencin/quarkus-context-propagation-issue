package com.test

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import jakarta.ws.rs.core.*
import java.util.*

class ThirsPartyServiceResource : QuarkusTestResourceLifecycleManager {

    private val wireMockServer: WireMockServer by lazy {
        WireMockServer(WireMockConfiguration().dynamicPort().notifier(ConsoleNotifier(true)))
    }


    override fun start(): Map<String, String> {
        wireMockServer.start()

        // It's sad the stubs are setup here, it should be done in the tests, injecting this instance
        wireMockServer.stubFor(
            get(urlMatching("/entities/.*"))
                .willReturn(
                    aResponse()
                        // we need to add a delay to simulate a real network here
                        .withFixedDelay(3000)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBody(
                            """
{
    "id": "${UUID.randomUUID()}",
    "nickname": "nickname"
}
                        """.trimIndent(),
                        ),
                ),
        )

        return mapOf(
            "tp-service-api/mp-rest/url" to wireMockServer.baseUrl(),
        )
    }

    override fun stop() {
        wireMockServer.start()
    }
}
