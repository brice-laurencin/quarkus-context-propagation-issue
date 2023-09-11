package com.test.clients.third_party_service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.HeaderParam
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

@RegisterRestClient(configKey = "tp-service-api")
@ApplicationScoped
//@Retry(maxRetries = 5, delay = 200, delayUnit = ChronoUnit.MILLIS, abortOn = [ClientWebApplicationException::class])
//@Timeout(value = 10, unit = ChronoUnit.SECONDS)
interface ThirdPartyRestClient {

    // ************************************************************************
    // ADMIN PROFILE
    // ************************************************************************

    @GET
    @Path("/entities/{id}")
    @Produces("application/json")
    fun getEntity(
            @HeaderParam("Authorization") authorization: String,
            @PathParam("id") id: Long,
    ): ServiceResponse?
    @GET
    @Path("/entities/{id}")
    @Produces("application/json")
    suspend fun getEntityCoroutine(
            @HeaderParam("Authorization") authorization: String,
            @PathParam("id") id: Long,
    ): ServiceResponse?
}
