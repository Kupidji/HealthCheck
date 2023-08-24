package com.example.domain.usecase.steps

import com.example.domain.AppDispatchers
import com.example.domain.repository.StepsRepository
import kotlinx.coroutines.withContext

class GetCountOfStepsForDayFromDb(private val repository : StepsRepository) {

    suspend fun execute() : Int = withContext(AppDispatchers.io) {
        return@withContext repository.getStepsForDay()
    }

}