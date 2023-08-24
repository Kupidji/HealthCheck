package com.example.data.weight.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.weight.entities.WeightForDb

@Entity(
    tableName = "weight"
)
data class WeightDbEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    var weight : Float,
    var date : Long,
) {

    fun toWeightForDb() : WeightForDb = WeightForDb(
        id = id,
        weight = weight,
        date = date,
    )

    companion object {

        fun fromWeight(weight: WeightForDb) : WeightDbEntity = WeightDbEntity(
            id = 0,
            weight = weight.weight,
            date = weight.date,
        )

        fun forUpdateWeight(weight: WeightForDb) : WeightDbEntity = WeightDbEntity(
            id = weight.id,
            weight = weight.weight,
            date = weight.date
        )

    }

}