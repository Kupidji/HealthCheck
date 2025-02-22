package com.example.data.heart.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.models.Heart


@Entity (
    tableName = "heart"
)
data class HeartDbEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    @ColumnInfo(name = "pressure_up")
    var pressureUp : Int,
    @ColumnInfo(name = "pressure_down")
    var pressureDown : Int,
    var pulse : Int,
    var date : Long,
) {

    fun toHeart() : Heart = Heart(
        id = id,
        pressureUp = pressureUp,
        pressureDown = pressureDown,
        pulse = pulse,
        date = date,
    )

    companion object {

        fun fromHeart(heart : Heart) : HeartDbEntity = HeartDbEntity(
            id = 0,
            pressureUp = heart.pressureUp,
            pressureDown = heart.pressureDown,
            pulse = heart.pulse,
            date = heart.date,
        )

        fun forUpdate(heart: Heart) : HeartDbEntity = HeartDbEntity(
            id = heart.id,
            pressureUp = heart.pressureUp,
            pressureDown = heart.pressureDown,
            pulse = heart.pulse,
            date = heart.date,
        )

    }
}