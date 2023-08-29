package com.example.data.sleep.room

import com.example.domain.repository.SleepRepository
import com.example.data.sleep.entities.SleepForDb
import com.example.data.sleep.room.entities.SleepDbEntity
import com.example.data.sleep.room.entities.SleepDbEntity.Companion.fromSleep
import com.example.data.sleep.room.entities.SleepDbEntity.Companion.updateSleep
import com.example.domain.models.Sleep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomSleepRepository(
    val sleepDao: SleepDao
) : SleepRepository {

    override suspend fun insertTimeOfSleep(sleep: Sleep) {
        sleepDao.insertTimeOfSleep(fromSleep(toSleepForDb(sleep)))
    }

    override suspend fun updateTimeOfSleep(sleep: Sleep) {
        sleepDao.updateTimeOfSleep(updateSleep(toSleepForDb(sleep)))
    }

    override fun getLastIdAndDate(): Flow<Sleep> {
        return sleepDao.getTimeOfSleepForDay().map { sleepDbEntity ->
            toSleep(sleepDbEntity)
        }
    }

    override fun getTimeOfSleepForDay(): Flow<Long> {
        return sleepDao.getTimeOfSleepForDay().map { sleepDbEntity ->
            sleepDbEntity.timeOfSleep
        }
    }

    override fun getTimeOfSleepForWeek() : Flow<List<Long>> {
        return sleepDao.getTimeOfSleepForWeek().map { list ->
            list.map { sleepDbEntity ->
                sleepDbEntity.timeOfSleep
            }
        }
    }

    override fun getTimeOfSleepForMonth(): Flow<List<Long>> {
        return sleepDao.getTimeOfSleepForMonth().map { list ->
            list.map { sleepDbEntity ->
                sleepDbEntity.timeOfSleep
            }
        }
    }

    override fun getListForHistory(): Flow<List<Sleep>> {
        return sleepDao.getSleepListForHistory().map { list ->
            list.map { sleepDbEntity ->
                toSleep(sleepDbEntity)
            }
        }
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