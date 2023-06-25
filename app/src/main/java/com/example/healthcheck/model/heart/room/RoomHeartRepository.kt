package com.example.healthcheck.model.heart.room

import com.example.healthcheck.model.heart.HeartRepository
import com.example.healthcheck.model.heart.entities.Heart
import com.example.healthcheck.model.heart.room.entities.HeartDbEntity.Companion.forUpdate
import com.example.healthcheck.model.heart.room.entities.HeartDbEntity.Companion.fromHeart
import com.example.healthcheck.model.steps.entities.Steps
import com.example.healthcheck.model.steps.room.entities.StepsDbEntity

class RoomHeartRepository(
    val heartDao: HeartDao
) : HeartRepository {

    override suspend fun insertCardio(heart: Heart) {
        heartDao.insertCardio(fromHeart(heart))
    }

    override fun getCardioForDay() : List<Heart> {
        return heartDao.getCardioForDay().map { heart ->
            heart.toHeart()
        }
    }

    override suspend fun updateCardio(heart: Heart) {
        heartDao.updateCardio(forUpdate(heart))
    }

}
