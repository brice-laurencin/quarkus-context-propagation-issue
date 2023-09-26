package com.test.clients.third_party_service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import java.io.File

@RegisterRestClient(configKey = "tp-service-api")
@ApplicationScoped
interface ThirdPartyRestClient {
    @GET
    @Path("/hello/get-file-as-third-party")
    fun getFile(): File

    @GET
    @Path("/hello/get-file-as-third-party")
    fun getBytes(): ByteArray

    @GET
    @Path("/hello/get-file-as-third-party")
    fun getResponse(): Response
}
