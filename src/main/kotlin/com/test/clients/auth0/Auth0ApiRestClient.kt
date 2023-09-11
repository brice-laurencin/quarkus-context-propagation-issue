package com.test.clients.auth0

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType.APPLICATION_JSON
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

@RegisterRestClient(configKey = "auth0-api")
@ApplicationScoped
interface Auth0ApiRestClient {


    @POST
    @Path("/oauth/token")
    @Produces(APPLICATION_JSON)
    fun createOauthToken(oauthTokenRequest: Auth0TokenRequest): Auth0TokenResponse
}
