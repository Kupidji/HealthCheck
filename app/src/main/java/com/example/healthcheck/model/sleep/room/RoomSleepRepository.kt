package com.example.healthcheck.model.sleep.room

import com.example.healthcheck.model.sleep.SleepRepository
import com.example.healthcheck.model.sleep.entities.Sleep
import com.example.healthcheck.model.sleep.room.entities.SleepDbEntity.Companion.fromSleep
import com.example.healthcheck.model.sleep.room.entities.SleepDbEntity.Companion.updateSleep

class RoomSleepRepository(
    val sleepDao: SleepDao
) : SleepRepository {

    override suspend fun insertTimeOfSleep(sleep: Sleep) {
        sleepDao.insertTimeOfSleep(fromSleep(sleep))
    }

    override suspend fun updateTimeOfSleep(sleep: Sleep) {
        sleepDao.updateTimeOfSleep(updateSleep(sleep))
    }

    override fun getTimeOfSleepForWeek() : List<Sleep> {
        return sleepDao.getTimeOfSleepForWeek().map { sleep ->
            sleep.toSleep()
        }
    }

    override fun getTimeOfSleepForMonth(): List<Sleep> {
        return sleepDao.getTimeOfSleepForMonth().map { sleep ->
            sleep.toSleep()
        }
    }

    override fun getTimeOfSleepForDay(): List<Sleep> {
        return sleepDao.getTimeOfSleepForDay().map { sleep ->
            sleep.toSleep()
        }
    }

}