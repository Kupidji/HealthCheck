package com.example.domain.usecase.sleep

import com.example.domain.AppDispatchers
import com.example.domain.repository.SleepStorageRepository
import kotlinx.coroutines.withContext

class GetGoToSleepTime(private val repository: SleepStorageRepository) {

    suspend fun execute() = withContext(AppDispatchers.io) {
        return@withContext repository.getTimeGoToSleep()
    }

}