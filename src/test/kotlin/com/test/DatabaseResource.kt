package com.test

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.PostgreSQLContainer
import java.util.*

class DatabaseResource : QuarkusTestResourceLifecycleManager {

    private val DATABASE = PostgreSQLContainer("postgres:14.2")
        .withDatabaseName("thing_db")
        .withUsername("test")
        .withPassword("test")
        .withExposedPorts(5432)

    override fun start(): Map<String?, String?>? {
        DATABASE.start()
        println("##################################")
        println(DATABASE.jdbcUrl)
        println("##################################")
        return mapOf(
                "quarkus.datasource.jdbc.url" to DATABASE.jdbcUrl,
                "quarkus.datasource.username" to "test",
                "quarkus.datasource.password" to "test",
                )
    }

    override fun stop() {
        DATABASE.stop()
    }
}
