package com.example.data.heart.room

import com.example.data.heart.room.entities.HeartDbEntity.Companion.forUpdate
import com.example.data.heart.room.entities.HeartDbEntity.Companion.fromHeart
import com.example.domain.models.Heart
import com.example.domain.repository.HeartRepository


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
