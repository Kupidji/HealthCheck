package com.example.domain.repository

import com.example.domain.models.Weight
import kotlinx.coroutines.flow.Flow


interface WeightRepository {

    suspend fun insertWeight(weight: Weight)

    suspend fun updateWeight(weight: Weight)

    fun getWeightForDay() : Flow<List<Float>>

    fun getWeightForWeek() : Flow<List<Float>>

    fun getWeightForMonth() : Flow<List<Float>>

    fun getWeightLastIdAndDate() : Flow<List<Weight>>

    fun getListForHistory() : Flow<List<Weight>>

}