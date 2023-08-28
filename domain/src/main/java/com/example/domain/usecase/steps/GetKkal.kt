package com.example.domain.usecase.steps

import com.example.domain.AppDispatchers
import com.example.domain.repository.WeightRepository
import com.example.domain.usecase.GetWeightForMonthFromDb
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

class GetKkal(private val repository : WeightRepository) {

    suspend fun execute(steps: Int): Int = withContext(AppDispatchers.default) {
        val getWeightForMonthFromDb = GetWeightForMonthFromDb(repository)
        var result = 0
        getWeightForMonthFromDb.execute().collect { weight ->
            result = ((steps.toFloat()/1300) * 0.52 * weight).toInt()
        }
        return@withContext result
    }

}