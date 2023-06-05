package com.example.healthcheck.model.weight.room

import com.example.healthcheck.model.weight.WeightRepository
import com.example.healthcheck.model.weight.entities.Weight
import com.example.healthcheck.model.weight.room.entities.WeightDbEntity.Companion.fromWeight

class RoomWeightRepository(
    val weightDao: WeightDao
) : WeightRepository{
    override suspend fun insertWeight(weight: Weight) {
        weightDao.insertWeight(fromWeight(weight))
    }

    override fun getWeightForWeek(): List<Weight> {
        return weightDao.getWeightForWeek().map { weight ->
            weight.toWeight()
        }
    }

    override fun getWeightForMonth(): List<Weight> {
        return weightDao.getWeightForWeek().map { weight ->
            weight.toWeight()
        }
    }


}