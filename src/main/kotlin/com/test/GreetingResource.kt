package com.test

import com.test.clients.third_party_service.ThirdPartyRestClient
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.HttpHeaders
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.jboss.resteasy.reactive.RestResponse
import java.io.File

@Path("/hello")
class GreetingResource {

    @Inject
    @field: RestClient
    lateinit var tpRestClient: ThirdPartyRestClient

    @Path("/get-file-as-third-party")
    @GET
    fun getFileAsThirdParty(
    ): RestResponse<ByteArray>? {
        val file = File(GreetingResource::class.java.getResource("/document.pdf")!!.toURI())
        val ok = RestResponse.ok(file.readBytes(), "application/pdf")
        ok.headers[HttpHeaders.CONTENT_DISPOSITION] = listOf("attachment; filename=\"temp.pdf\"")
        return ok
    }

    @Path("/get-file-as-file")
    @GET
    fun getFile(): RestResponse<ByteArray>? {
        val file = tpRestClient.getFile()
        val ok = RestResponse.ok(file.readBytes(), "application/pdf")
        ok.headers[HttpHeaders.CONTENT_DISPOSITION] = listOf("attachment; filename=\"temp.pdf\"")
        return ok
    }
    @Path("/get-file-as-bytes")
    @GET
    fun getBytes(): RestResponse<ByteArray>? {
        val content = tpRestClient.getBytes()
        val ok = RestResponse.ok(content, "application/pdf")
        ok.headers[HttpHeaders.CONTENT_DISPOSITION] = listOf("attachment; filename=\"temp.pdf\"")
        return ok
    }
    @Path("/get-file-as-response")
    @GET
    fun getResponse(): RestResponse<ByteArray>? {
        val response = tpRestClient.getResponse()
        val ok = RestResponse.ok(response.readEntity(ByteArray::class.java), "application/pdf")
        ok.headers[HttpHeaders.CONTENT_DISPOSITION] = listOf("attachment; filename=\"temp.pdf\"")
        return ok
    }

}
