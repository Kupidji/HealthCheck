package com.example.healthcheck.model.weight.room

import com.example.healthcheck.model.weight.WeightRepository
import com.example.healthcheck.model.weight.entities.Weight
import com.example.healthcheck.model.weight.room.entities.WeightDbEntity
import com.example.healthcheck.model.weight.room.entities.WeightDbEntity.Companion.forUpdateWeight
import com.example.healthcheck.model.weight.room.entities.WeightDbEntity.Companion.fromWeight

class RoomWeightRepository(
    val weightDao: WeightDao
) : WeightRepository{
    override suspend fun insertWeight(weight: Weight) {
        weightDao.insertWeight(fromWeight(weight))
    }

    override suspend fun updateWeight(weight: Weight) {
        weightDao.updateWeight(forUpdateWeight(weight))
    }

    override suspend fun getLastWeight(): WeightDbEntity {
        return weightDao.getLastWeight()
    }

    override suspend fun getWeightForWeek(): List<Float> {
        var list = mutableListOf<Float>()
        var new = weightDao.getWeightForWeek().map { Weight ->
            Weight.toWeight()
            list.add(Weight.weight)
        }
        return list
    }

    override suspend fun getWeightForMonth(): List<Float> {
        var list = mutableListOf<Float>()
        var new = weightDao.getWeightForMonth().map { Weight ->
            Weight.toWeight()
            list.add(Weight.weight)
        }
        return list
    }


}