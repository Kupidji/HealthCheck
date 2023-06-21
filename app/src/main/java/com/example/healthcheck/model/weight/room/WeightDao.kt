package com.example.healthcheck.model.weight.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.healthcheck.model.steps.room.entities.StepsDbEntity
import com.example.healthcheck.model.weight.room.entities.WeightDbEntity

@Dao
interface WeightDao {

    @Insert
    suspend fun insertWeight(weightDbEntity: WeightDbEntity)

    @Query("SELECT * FROM weight ORDER BY id deSC LIMIT 7")
    fun getWeightForWeek() : List<WeightDbEntity>

    @Query("SELECT * FROM weight ORDER BY id deSC LIMIT 30")
    fun getWeightForMonth() : List<WeightDbEntity>

    @Query("SELECT * FROM weight order by id desc limit 1")
    suspend fun getLastWeight() : WeightDbEntity

    @Update
    suspend fun updateWeight(weightDbEntity: WeightDbEntity)

}