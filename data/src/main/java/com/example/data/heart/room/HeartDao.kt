package com.example.data.heart.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.data.heart.room.entities.HeartDbEntity

@Dao
interface HeartDao {

    @Insert
    suspend fun insertCardio(heartDbEntity: HeartDbEntity)

    @Query("SELECT * FROM heart ORDER BY id DESC LIMIT 1")
    fun getCardioForDay() : HeartDbEntity
    @Query("SELECT * FROM heart ORDER BY id DESC LIMIT 7")
    suspend fun getCadrioForWeek() : List<HeartDbEntity>

    @Query("SELECT * FROM heart ORDER BY id DESC LIMIT 30")
    suspend fun getCadrioForMonth() : List<HeartDbEntity>

    @Update
    suspend fun updateCardio(heartDbEntity: HeartDbEntity)

    @Delete
    suspend fun deleteCardio(heartDbEntity: HeartDbEntity)

    @Query("SELECT * FROM heart")
    suspend fun getCadrioForHistory() : List<HeartDbEntity>

}