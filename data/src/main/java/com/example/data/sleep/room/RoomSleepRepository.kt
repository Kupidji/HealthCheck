package com.example.data.sleep.room

import com.example.domain.repository.SleepRepository
import com.example.data.sleep.entities.SleepForDb
import com.example.data.sleep.room.entities.SleepDbEntity
import com.example.data.sleep.room.entities.SleepDbEntity.Companion.fromSleep
import com.example.data.sleep.room.entities.SleepDbEntity.Companion.updateSleep
import com.example.domain.models.Sleep

class RoomSleepRepository(
    val sleepDao: SleepDao
) : SleepRepository {

    override suspend fun insertTimeOfSleep(sleep: Sleep) {
        sleepDao.insertTimeOfSleep(fromSleep(toSleepForDb(sleep)))
    }

    override suspend fun updateTimeOfSleep(sleep: Sleep) {
        sleepDao.updateTimeOfSleep(updateSleep(toSleepForDb(sleep)))
    }

    override suspend fun getTimeOfSleepForWeek() : List<Long> {
        var list = mutableListOf<Long>()
        sleepDao.getTimeOfSleepForWeek().map { sleepDbEntity ->
            toSleep(sleepDbEntity)
            list.add(sleepDbEntity.timeOfSleep)
        }
        return list
    }

    override suspend fun getTimeOfSleepForDay(): Long {
        return sleepDao.getTimeOfSleepForDay().timeOfSleep
    }

    override suspend fun getTimeOfSleepForMonth(): List<Long> {
        var list = mutableListOf<Long>()
        sleepDao.getTimeOfSleepForMonth().map { sleepDbEntity ->
            toSleep(sleepDbEntity)
            list.add(sleepDbEntity.timeOfSleep)
        }
        return list
    }

    override suspend fun getListForHistory(): List<Sleep> {
        return sleepDao.getSleepListForHistory().map { sleepDbEntity ->
            toSleep(sleepDbEntity)
        }
    }

    override suspend fun getLastDate(): Sleep {
        val sleep = sleepDao.getTimeOfSleepForDay()
        return Sleep (
            id = sleep.id,
            timeOfSleep =  sleep.timeOfSleep,
            date = sleep.date
        )
    }

    private fun toSleepForDb(sleep : Sleep) : SleepForDb {
        return SleepForDb(
            id = sleep.id,
            timeOfSleep = sleep.timeOfSleep,
            date = sleep.date,
        )
    }

    private fun toSleep(sleepDbEntity: SleepDbEntity) : Sleep {
        return Sleep(
            id = sleepDbEntity.id,
            timeOfSleep = sleepDbEntity.timeOfSleep,
            date = sleepDbEntity.date,
        )
    }

}