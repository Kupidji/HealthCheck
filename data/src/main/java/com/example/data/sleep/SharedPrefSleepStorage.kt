package com.example.data.sleep

import android.content.Context
import com.example.data.GO_TO_BED_TIME
import com.example.data.SLEEP
import com.example.data.WAKE_UP_TIME
import com.example.domain.repository.SleepStorageRepository

class SharedPrefSleepStorage(context : Context) : SleepStorageRepository {

    private val storage = context.applicationContext.getSharedPreferences(SLEEP, Context.MODE_PRIVATE)
    private val storageEdit = storage.edit()

    override suspend fun setWakeUpTime(time: Long) {
        storageEdit.putLong(WAKE_UP_TIME, time).apply()
    }

    override suspend fun getWakeUpTime(): Long {
        return storage.getLong(WAKE_UP_TIME, 0L)
    }

    override suspend fun setTimeGoToSleep(time: Long) {
        storageEdit.putLong(GO_TO_BED_TIME, time).apply()
    }

    override suspend fun getTimeGoToSleep(): Long {
        return storage.getLong(GO_TO_BED_TIME, 0L)
    }


}