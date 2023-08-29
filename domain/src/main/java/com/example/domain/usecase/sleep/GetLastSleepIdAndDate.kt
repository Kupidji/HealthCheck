package com.example.domain.usecase.sleep

import com.example.domain.AppDispatchers
import com.example.domain.models.Sleep
import com.example.domain.repository.SleepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetLastSleepIdAndDate(private val repository: SleepRepository) {

    suspend fun execute() : Flow<Sleep> = withContext(AppDispatchers.io) {
        return@withContext repository.getLastIdAndDate().map { list ->
            var ourSleep = Sleep(0,0,0)
            for (sleep in list) {
                ourSleep = sleep
            }
            ourSleep
        }
    }

}