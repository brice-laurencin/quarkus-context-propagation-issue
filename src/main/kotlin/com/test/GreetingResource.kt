package com.test

import com.test.clients.third_party_service.ThirdPartyService
import com.test.thing.service.ThingService
import com.test.thing.service.ThingStatus
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import kotlinx.coroutines.runBlocking
import org.eclipse.microprofile.context.ManagedExecutor
import java.util.*

@Path("/hello")
class GreetingResource {

    @Inject
    lateinit var thingService: ThingService


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    fun getSome() = thingService.getFromThirdParty(ThingStatus.OK)

}
