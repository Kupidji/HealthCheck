package com.example.healthcheck.model.heart.room

import com.example.healthcheck.model.heart.HeartRepository
import com.example.healthcheck.model.heart.entities.Heart
import com.example.healthcheck.model.heart.room.entities.HeartDbEntity.Companion.fromHeart

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

}
