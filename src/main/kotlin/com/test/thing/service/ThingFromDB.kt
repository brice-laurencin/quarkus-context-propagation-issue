package com.test.thing.service

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "thing")
data class ThingFromDB(
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    var status: ThingStatus? = null,

    @Column(name = "reason")
    var reason: String? = null,
): PanacheEntityBase

enum class ThingStatus {
OK,KO
}

data class ThingModel(
    var id: Long? = null,
    var name: String? = null,
    var status: ThingStatus? = null,
    var reason: String? = null,
) {
    constructor(fromDb: ThingFromDB) : this() {
        id = fromDb.id
        status = fromDb.status
        reason = fromDb.reason
    }
}
