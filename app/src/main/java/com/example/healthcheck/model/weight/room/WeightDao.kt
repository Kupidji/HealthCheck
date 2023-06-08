package com.example.healthcheck.model.weight.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.healthcheck.model.weight.room.entities.WeightDbEntity

@Dao
interface WeightDao {

    @Insert
    suspend fun insertWeight(weightDbEntity: WeightDbEntity)

    //TODO доделать запрос для веса
    @Query("SELECT * FROM weight ORDER BY id deSC LIMIT 7")
    fun getWeightForWeek() : List<WeightDbEntity>

    //TODO доделать запрос для веса
    @Query("SELECT * FROM weight ORDER BY id deSC LIMIT 30")
    fun getWeightForMonth() : List<WeightDbEntity>

}