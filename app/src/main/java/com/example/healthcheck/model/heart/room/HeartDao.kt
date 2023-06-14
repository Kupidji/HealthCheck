package com.example.healthcheck.model.heart.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.healthcheck.model.heart.room.entities.HeartDbEntity

@Dao
interface HeartDao {

    @Insert
    suspend fun insertCardio(heartDbEntity: HeartDbEntity)

    //TODO доделать запрос для вытягивание инфы за прошлый день
    @Query("SELECT * FROM heart ORDER BY id DESC LIMIT 1")
    fun getCardioForDay() : List<HeartDbEntity>
}