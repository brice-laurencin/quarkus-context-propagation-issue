package com.test.thing.service

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ThingRepository: PanacheRepository<ThingFromDB> {
    fun findByStatus(status: ThingStatus): List<ThingFromDB> {
        return find("status", status).list()
    }
}
