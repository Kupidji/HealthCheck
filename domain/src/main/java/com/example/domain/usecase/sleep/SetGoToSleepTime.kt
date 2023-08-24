package com.example.domain.usecase.sleep

import com.example.domain.AppDispatchers
import com.example.domain.repository.SleepStorageRepository
import kotlinx.coroutines.withContext

class SetGoToSleepTime(private val repository: SleepStorageRepository) {

    suspend fun execute(time : Long) = withContext(AppDispatchers.io) {
        repository.setTimeGoToSleep(time = time)
    }

}