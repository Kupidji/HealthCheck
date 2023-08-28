package com.example.domain.usecase.sleep

import com.example.domain.AppDispatchers
import com.example.domain.models.Sleep
import com.example.domain.repository.SleepRepository
import kotlinx.coroutines.withContext

class GetSleepListForHistory(private val repository: SleepRepository) {

    suspend fun execute() : List<Sleep> = withContext(AppDispatchers.io) {
        return@withContext repository.getListForHistory()
    }

}