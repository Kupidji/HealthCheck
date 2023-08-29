package com.example.domain.usecase.sleep

import com.example.domain.AppDispatchers
import com.example.domain.models.Sleep
import com.example.domain.repository.SleepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetLastSleepIdAndDate(private val repository: SleepRepository) {

    suspend fun execute() : Flow<Sleep> = withContext(AppDispatchers.io) {
        return@withContext repository.getLastIdAndDate()
    }

}