package com.example.domain.repository

import com.example.domain.models.Weight


interface WeightRepository {

    suspend fun insertWeight(weight: Weight)

    suspend fun updateWeight(weight: Weight)

    suspend fun getWeightForDay() : Weight

    suspend fun getWeightForWeek() : List<Float>

    suspend fun getWeightForMonth() : List<Float>

    suspend fun getListForHistory() : List<Weight>

}