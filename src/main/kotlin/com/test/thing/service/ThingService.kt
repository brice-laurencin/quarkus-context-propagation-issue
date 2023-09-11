package com.test.thing.service

import com.test.clients.third_party_service.ThirdPartyService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.eclipse.microprofile.context.ManagedExecutor
import kotlin.random.Random

@ApplicationScoped
class ThingService {
    @Inject
    @field:Default
    lateinit var thingRepository: ThingRepository

    @Inject
    @field:Default
    lateinit var thirdPartyService: ThirdPartyService

    @Inject
    @field:Default
    lateinit var managedExecutor: ManagedExecutor

    fun getFromThirdParty(status: ThingStatus): List<ThingModel> {
        return thingRepository.findByStatus(status).map {
            val thingModel = ThingModel(it)
            thingModel.name = thirdPartyService.getThingName(thingModel)!!.nickname
            thingModel
        }
    }

    fun getFromThirdPartyParallel(status: ThingStatus): List<ThingModel> {
        return thingRepository.findByStatus(status).parallelStream().map {
            val thingModel = ThingModel(it)
            thingModel.name = thirdPartyService.getThingName(thingModel)!!.nickname
            thingModel
        }.toList()
    }

    fun getFromThirdPartyNoDb(status: ThingStatus): List<ThingModel> {
        val fakeList = ArrayList<ThingModel>()
        repeat(500) {
            fakeList.add(ThingModel(
                    Random.nextLong(),
                    status = ThingStatus.OK
            ))
        }
        return fakeList.parallelStream().map {
            it.name = thirdPartyService.getThingName(it)!!.nickname
            it
        }.toList()
    }

    fun getFromThirdPartyManaged(status: ThingStatus): List<ThingModel> {
        return thingRepository.findByStatus(status).parallelStream().map {
            managedExecutor.supplyAsync {
                val thingModel = ThingModel(it)
                thingModel.name = thirdPartyService.getThingName(thingModel)!!.nickname
                thingModel
            }
        }.map { it.get() }.toList()
    }

    suspend fun getFromThirdPartyCoroutine(status: ThingStatus): List<ThingModel> {
        return thingRepository.findByStatus(status).parallelStream().map {
                coroutineScope {
                    val thingModel = ThingModel(it)
                    thingModel.name = thirdPartyService.getThingNameCoroutine(thingModel)!!.nickname
                    thingModel
                }

            }.toList()

    }
}
