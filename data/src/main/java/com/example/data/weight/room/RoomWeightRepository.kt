package com.example.data.weight.room

import com.example.data.weight.entities.WeightForDb
import com.example.data.weight.room.entities.WeightDbEntity.Companion.forUpdateWeight
import com.example.data.weight.room.entities.WeightDbEntity.Companion.fromWeight
import com.example.domain.models.Weight
import com.example.domain.repository.WeightRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class RoomWeightRepository(
    val weightDao: WeightDao
) : WeightRepository {
    override suspend fun insertWeight(weight: Weight) {
        weightDao.insertWeight(fromWeight(weight = toWeightForDb(weight)))
    }

    override suspend fun updateWeight(weight: Weight) {
        weightDao.updateWeight(forUpdateWeight(weight = toWeightForDb(weight)))
    }

    override fun getWeightForDay(): Flow<Float> {
        return weightDao.getLastWeight().map { weightDbEntity ->
            weightDbEntity.weight
        }

    }

    override fun getWeightForWeek(): Flow<List<Float>> {
        return weightDao.getWeightForWeek().map { list ->
            list.map { weightDbEntity ->
                weightDbEntity.weight
            }
        }
    }

    override fun getWeightForMonth(): Flow<List<Float>> {
        return weightDao.getWeightForMonth().map { list ->
            list.map { weightDbEntity ->
                weightDbEntity.weight
            }
        }
    }

    override fun getWeightLastIdAndDate(): Flow<Weight> {
        return weightDao.getLastWeight().map { weightDbEntity ->
            Weight (
                id = weightDbEntity.id,
                weight = weightDbEntity.weight,
                date = weightDbEntity.date
            )
        }
    }

    override fun getListForHistory() : Flow<List<Weight>> {
        return weightDao.getListForHistory().map { list ->
            list.map { weightDbEntity ->
                Weight(
                    id = weightDbEntity.id,
                    weight = weightDbEntity.weight,
                    date = weightDbEntity.date
                )
            }
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