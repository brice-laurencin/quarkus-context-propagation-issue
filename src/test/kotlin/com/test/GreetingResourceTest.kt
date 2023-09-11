package com.test

import com.test.thing.service.ThingFromDB
import com.test.thing.service.ThingRepository
import com.test.thing.service.ThingStatus
import io.quarkus.narayana.jta.QuarkusTransaction
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import jakarta.inject.Inject
import org.hamcrest.CoreMatchers.`is`
import org.junit.Ignore
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis


@QuarkusTest
@QuarkusTestResource(DatabaseResource::class)
@QuarkusTestResource(Auth0ServiceResource::class)
@QuarkusTestResource(ThirsPartyServiceResource::class)
class GreetingResourceTest {

    @Inject
    lateinit var thingRepository: ThingRepository

    @BeforeEach
    fun setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        QuarkusTransaction.begin()
        thingRepository.deleteAll()
        repeat(500) {
            val thing = ThingFromDB(status = ThingStatus.OK, reason = "this is $it")
            thingRepository.persist(thing)
        }
        QuarkusTransaction.commit()
    }

    @Test
    @Ignore
    fun testHelloEndpoint() {
        val count = measureTimeMillis {
            given()
                    .`when`().get("/hello")
                    .then()
                    .statusCode(200)
        }
        println("time taken: $count")
    }

    @Test
    fun testHelloParallelEndpoint() {
        val count = measureTimeMillis {
            given()
                    .`when`().get("/hello/parallel")
                    .then()
                    .statusCode(200)
        }
        println("time taken: $count")
    }
    @Test
    fun testHelloManagedEndpoint() {
        val count = measureTimeMillis {
            given()
                    .`when`().get("/hello/managed")
                    .then()
                    .statusCode(200)
        }
        println("time taken: $count")
    }
    @Test
    fun testHelloCoroutine() {
        val count = measureTimeMillis {
            given()
                    .`when`().get("/hello/coroutine ")
                    .then()
                    .statusCode(200)
        }
        println("time taken: $count")
    }
    @Test
    fun testHelloNoDb() {
        val count = measureTimeMillis {
            given()
                    .`when`().get("/hello/noDb ")
                    .then()
                    .statusCode(200)
        }
        println("time taken: $count")
    }

}
