package com.example.data.sleep.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.data.sleep.room.entities.SleepDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepDao {

    @Insert
    suspend fun insertTimeOfSleep(sleepDbEntity: SleepDbEntity)

    @Update
    suspend fun updateTimeOfSleep(sleepDbEntity: SleepDbEntity)

    @Query("SELECT * FROM Sleep ORDER BY id DESC LIMIT 1")
    fun getTimeOfSleepForDay() : Flow<SleepDbEntity>

    @Query("SELECT * FROM Sleep ORDER BY id DESC LIMIT 7")
    fun getTimeOfSleepForWeek() : Flow<List<SleepDbEntity>>

    @Query("SELECT * FROM Sleep ORDER BY id DESC LIMIT 30")
    fun getTimeOfSleepForMonth() : Flow<List<SleepDbEntity>>

    @Query("SELECT * FROM Sleep")
    fun getSleepListForHistory() : Flow<List<SleepDbEntity>>

}