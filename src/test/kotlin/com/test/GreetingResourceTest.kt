package com.test

import care.resilience.billing.S3TestResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Test


@QuarkusTest
@QuarkusTestResource(S3TestResource::class)
class GreetingResourceTest {

    @Test
    fun testExtensionClient() {
        given().`when`().get("/writeWithExtensionClient").then().statusCode(204)
    }
    @Test
    fun testManualClient() {
        given().`when`().get("/writeWithManualClient").then().statusCode(204)
    }
}
