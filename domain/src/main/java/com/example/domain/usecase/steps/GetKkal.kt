package com.example.domain.usecase.steps

import com.example.domain.AppDispatchers
import com.example.domain.repository.WeightRepository
import com.example.domain.usecase.GetWeightForMonthFromDb
import kotlinx.coroutines.withContext

class GetKkal(private val repository : WeightRepository) {

    suspend fun execute(steps: Int): Int = withContext(AppDispatchers.default) {
        val getWeightForMonthFromDb = GetWeightForMonthFromDb(repository)
        val weight = getWeightForMonthFromDb.execute()
        return@withContext ((steps.toFloat()/1300) * 0.52 * weight).toInt()
    }

}