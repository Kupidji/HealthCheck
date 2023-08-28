package com.example.data.heart.room

import com.example.data.heart.room.entities.HeartDbEntity.Companion.forUpdate
import com.example.data.heart.room.entities.HeartDbEntity.Companion.fromHeart
import com.example.domain.models.Heart
import com.example.domain.repository.HeartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class RoomHeartRepository(
    val heartDao: HeartDao
) : HeartRepository {

    override suspend fun insertCardio(heart: Heart) {
        heartDao.insertCardio(fromHeart(heart))
    }

    override suspend fun updateCardio(heart: Heart) {
        heartDao.updateCardio(forUpdate(heart))
    }

    override suspend fun deleteCardio(heart: Heart) {
        heartDao.deleteCardio(fromHeart(heart))
    }

    override fun getCardioForWeek(): Flow<List<Heart>> {
        return heartDao.getCadrioForWeek().map { list ->
            list.map { heart ->
                heart.toHeart()
            }

        }
    }

    override fun getCardioForMonth(): Flow<List<Heart>> {
        return heartDao.getCadrioForMonth().map { list ->
            list.map { heart ->
                heart.toHeart()
            }
        }
    }

    override fun getCardioForDay() : Flow<Heart> {
        return heartDao.getCardioForDay().map { heart ->
            heart.toHeart()
        }
    }

    override fun getListCardioForLastNote(): Flow<List<Heart>> {
        return heartDao.getCadrioForWeek().map { list ->
            list.map { heart ->
                heart.toHeart()
            }
        }
    }

    override fun getListCardioForHistory(): Flow<List<Heart>> {
        return heartDao.getCadrioForHistory().map {  list ->
            list.map { heart ->
                heart.toHeart()
            }
        }
    }

}
