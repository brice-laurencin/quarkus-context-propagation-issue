package com.test.clients.third_party_service

import com.test.clients.auth0.Auth0TokenService
import com.test.thing.service.ThingModel
import io.micrometer.core.annotation.Counted
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.context.control.ActivateRequestContext
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.jboss.logging.Logger

@ApplicationScoped
class ThirdPartyService {

    @Inject
    @field: Default
    lateinit var logger: Logger

    @Inject
    @field: Default
    lateinit var auth0TokenService: Auth0TokenService

    @Inject
    @field: RestClient
    lateinit var tpRestClient: ThirdPartyRestClient


    @ConfigProperty(name = "auth0-api.tp-service.client-id")
    lateinit var auth0ClientId: String

    @ConfigProperty(name = "auth0-api.tp-service.client-secret")
    lateinit var auth0ClientSecret: String


    fun getThingName(thingModel: ThingModel): ServiceResponse? {

        val token = auth0TokenService.getAPIToken(auth0ClientId, auth0ClientSecret)

        try {
            val tpResponse = tpRestClient.getEntity("BEARER $token", thingModel.id!!)
            logger.info("Got entity [${thingModel.id}] from TP Service.")

            return tpResponse
        } catch (e: Exception) {
            throw Exception(
                    "Unable to get entity with ID ${thingModel.id}",
                    e,
            )
        }
    }
}
