package com.example.domain.usecase.sleep

import com.example.domain.AppDispatchers
import com.example.domain.models.Sleep
import com.example.domain.repository.SleepRepository
import kotlinx.coroutines.withContext

class InsertSleep(private val repository: SleepRepository) {

    suspend fun execute(sleep : Sleep) = withContext(AppDispatchers.io) {
        repository.insertTimeOfSleep(sleep = sleep)
    }

}