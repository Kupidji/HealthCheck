package com.example.data.steps

import android.content.Context
import android.content.SharedPreferences
import com.example.data.STEPS
import com.example.data.STEPS_TARGET
import com.example.domain.repository.StepsStorageRepository

class SharedPrefStepsStorage(private val context : Context) : StepsStorageRepository {

    private val storage : SharedPreferences = context.applicationContext.getSharedPreferences(STEPS, Context.MODE_PRIVATE)
    override suspend fun setTarget(target: Int) {
        storage.edit().putInt(STEPS_TARGET, target).apply()
    }

    override suspend fun getCurrentTarget(): Int {
        return storage.getInt(STEPS_TARGET, 10000)
    }

}