package com.example.healthcheck.model.weight

import com.example.healthcheck.model.steps.entities.Steps
import com.example.healthcheck.model.steps.room.entities.StepsDbEntity
import com.example.healthcheck.model.weight.entities.Weight
import com.example.healthcheck.model.weight.room.entities.WeightDbEntity

interface WeightRepository {

    suspend fun insertWeight(weight: Weight)

    suspend fun getWeightForWeek() : List<Float>

    suspend fun getWeightForMonth() : List<Float>

    suspend fun updateWeight(weight: Weight)

    suspend fun getLastWeight() : WeightDbEntity

}