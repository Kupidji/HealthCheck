package com.example.data.weight.room

import com.example.data.weight.entities.WeightForDb
import com.example.data.weight.room.entities.WeightDbEntity.Companion.forUpdateWeight
import com.example.data.weight.room.entities.WeightDbEntity.Companion.fromWeight
import com.example.domain.models.Weight
import com.example.domain.repository.WeightRepository


class RoomWeightRepository(
    val weightDao: WeightDao
) : WeightRepository {
    override suspend fun insertWeight(weight: Weight) {
        weightDao.insertWeight(fromWeight(weight = toWeightForDb(weight)))
    }

    override suspend fun updateWeight(weight: Weight) {
        weightDao.updateWeight(forUpdateWeight(weight = toWeightForDb(weight)))
    }

    override suspend fun getWeightForDay(): Weight {
        val weight = weightDao.getLastWeight()
        return Weight(
            weight.id,
            weight.weight,
            weight.date,
        )
    }

    override suspend fun getWeightForWeek(): List<Float> {
        var list = mutableListOf<Float>()
        var new = weightDao.getWeightForWeek().map { Weight ->
            Weight.toWeightForDb()
            list.add(Weight.weight)
        }
        return list
    }

    override suspend fun getWeightForMonth(): List<Float> {
        var list = mutableListOf<Float>()
        var new = weightDao.getWeightForMonth().map { Weight ->
            Weight.toWeightForDb()
            list.add(Weight.weight)
        }
        return list
    }

    override suspend fun getListForHistory() : List<Weight> {
        return weightDao.getListForHistory().map { weightDbEntity ->
            Weight(
                id = weightDbEntity.id,
                weight = weightDbEntity.weight,
                date = weightDbEntity.date
            )
        }
    }

    private fun toWeightForDb(weight: Weight) : WeightForDb {
        return WeightForDb (
            id = weight.id,
            weight = weight.weight,
            date = weight.date,
        )
    }

}