package com.test.clients.auth0

import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.jboss.logging.Logger
import java.time.LocalDateTime
import java.time.ZoneOffset

@ApplicationScoped
class Auth0TokenService {

    @Inject
    @field: Default
    lateinit var logger: Logger

    @Inject
    @field: RestClient
    lateinit var auth0ApiRestClient: Auth0ApiRestClient

    @ConfigProperty(name = "auth0-api.audience")
    lateinit var auth0Audience: String

    companion object {
        const val CLIENT_CREDENTIALS_GRANT_TYPE = "client_credentials"
        const val EXPIRATION_SHIFT_IN_SECONDS = 60L
    }

    fun getAPIToken(clientId: String, clientSecret: String): String? {
        // Should be cached but here for the sake of the demo, we do not care
        var auth0Token =
            // Retrieve token from Auth0
            getAuth0Token(clientId, clientSecret)


        val now = LocalDateTime.now(ZoneOffset.UTC)
        if (now.isAfter(auth0Token?.expiresAt)) {
            logger.infof("Auth0 API token of client id $clientId is expired (%s > %s)", now, auth0Token?.expiresAt)
            auth0Token = getAuth0Token(clientId, clientSecret)
        }
        return auth0Token?.accessToken
    }


    private fun getAuth0Token(clientId: String, clientSecret: String): Auth0TokenResponse? {
        logger.info("Get Auth0 token of client id $clientId")
        return try {
            val auth0Token: Auth0TokenResponse = auth0ApiRestClient.createOauthToken(
                Auth0TokenRequest(
                    grantType = CLIENT_CREDENTIALS_GRANT_TYPE,
                    audience = auth0Audience,
                    clientId = clientId,
                    clientSecret = clientSecret,
                ),
            )
            auth0Token.expiresAt = auth0Token.expiresIn?.let {
                LocalDateTime.now(ZoneOffset.UTC).plusSeconds(it).minusSeconds(EXPIRATION_SHIFT_IN_SECONDS)
            }
            auth0Token
        } catch (e: Exception) {
            throw Exception(
                "Unable to get Auth0 token", e
            )
        }
    }
}
