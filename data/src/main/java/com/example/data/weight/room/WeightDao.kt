package com.example.data.weight.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.data.weight.room.entities.WeightDbEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface WeightDao {

    @Insert
    suspend fun insertWeight(weightDbEntity: WeightDbEntity)

    @Update
    suspend fun updateWeight(weightDbEntity: WeightDbEntity)

    @Query("SELECT * FROM weight order by id desc limit 1")
    fun getLastWeight() : Flow<List<WeightDbEntity>>

    @Query("SELECT * FROM weight ORDER BY id deSC LIMIT 7")
    fun getWeightForWeek() : Flow<List<WeightDbEntity>>

    @Query("SELECT * FROM weight ORDER BY id deSC LIMIT 30")
    fun getWeightForMonth() : Flow<List<WeightDbEntity>>

    @Query("SELECT * FROM weight")
    fun getListForHistory() : Flow<List<WeightDbEntity>>



}