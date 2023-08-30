package com.example.domain.usecase.steps

import com.example.domain.AppDispatchers
import com.example.domain.repository.WeightRepository
import com.example.domain.usecase.GetWeightForMonthFromDb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetKkal(private val repository : WeightRepository) {

    suspend fun execute(steps: Int): Flow<Int> = withContext(AppDispatchers.default) {
        val weightFlow = withContext(AppDispatchers.io) {
            val getWeightForMonthFromDb = GetWeightForMonthFromDb(repository)
            return@withContext getWeightForMonthFromDb.execute()
        }

        return@withContext weightFlow.map { weight ->
            ((steps.toFloat()/1300) * 0.52 * weight).toInt()
        }

    }

}