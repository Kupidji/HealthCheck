package com.example.domain.usecase.sleep

import com.example.domain.AppDispatchers
import com.example.domain.repository.SleepStorageRepository
import kotlinx.coroutines.withContext

class GetWakeUpTime(private val repository: SleepStorageRepository) {

    suspend fun execute() : Long = withContext(AppDispatchers.io) {
        return@withContext repository.getWakeUpTime()
    }

}