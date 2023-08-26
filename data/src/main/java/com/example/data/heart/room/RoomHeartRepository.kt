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

    override suspend fun getCardioForWeek(): List<Heart> {
        return heartDao.getCadrioForWeek().map { heart ->
            heart.toHeart()
        }
    }

    override suspend fun getCardioForMonth(): List<Heart> {
        return heartDao.getCadrioForMonth().map { heart ->
            heart.toHeart()
        }
    }

    override suspend fun getCardioForDay() : Heart {
        return heartDao.getCardioForDay().toHeart()
    }

    override suspend fun updateCardio(heart: Heart) {
        heartDao.updateCardio(forUpdate(heart))
    }

    override suspend fun deleteCardio(heart: Heart) {
        heartDao.deleteCardio(fromHeart(heart))
    }

    override suspend fun getListCardioForLastNote(): List<Heart> {
        return heartDao.getCadrioForWeek().map { heart ->
            heart.toHeart()
        }
    }

    override suspend fun getListCardioForHistory(): List<Heart> {
        return heartDao.getCadrioForHistory().map { heart ->
            heart.toHeart()
        }
    }

}
