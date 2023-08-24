package com.example.domain.repository

interface SleepStorageRepository {

    suspend fun setWakeUpTime(time : Long)

    suspend fun getWakeUpTime() : Long

    suspend fun setTimeGoToSleep(time : Long)

    suspend fun getTimeGoToSleep() : Long

}