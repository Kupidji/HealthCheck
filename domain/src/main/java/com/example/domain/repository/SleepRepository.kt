package com.example.domain.repository

import com.example.domain.models.Sleep
import kotlinx.coroutines.flow.Flow

interface SleepRepository {

    suspend fun insertTimeOfSleep(sleep : Sleep)

    suspend fun updateTimeOfSleep(sleep: Sleep)

    fun getTimeOfSleepForDay() : Flow<Long>

    fun getTimeOfSleepForWeek() : Flow<List<Long>>

    fun getTimeOfSleepForMonth() : Flow<List<Long>>

    fun getListForHistory() : Flow<List<Sleep>>

    fun getLastIdAndDate() : Flow<Sleep>

}