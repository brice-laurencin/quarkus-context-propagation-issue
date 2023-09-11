package com.test

import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.event.Observes
import jakarta.ws.rs.core.Application
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType
import org.eclipse.microprofile.openapi.annotations.info.Info
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes
import org.eclipse.microprofile.openapi.annotations.servers.Server
import java.util.*

@OpenAPIDefinition(
    info = Info(
        title = "Demo Service API",
        description = "This API is demo",
        version = "1.0",
    ),
    servers = [
        Server(url = "http://0.0.0.0:15080"),
    ],
)
@SecuritySchemes(
    value = [
        SecurityScheme(
            securitySchemeName = "token",
            type = SecuritySchemeType.HTTP,
            scheme = "Bearer",
        ),
    ],
)
class Application : Application() {
    fun forceTimezoneUTC(
        @Observes startupEvent: StartupEvent?,
    ) {
        // System.setProperty("user.timezone", "UTC") // Doesn't work in dev mode
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }
}
