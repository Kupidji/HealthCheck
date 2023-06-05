package com.example.healthcheck.model.weight

import com.example.healthcheck.model.weight.entities.Weight

interface WeightRepository {

    suspend fun insertWeight(weight: Weight)

    fun getWeightForWeek() : List<Weight>

    fun getWeightForMonth() : List<Weight>

}