package com.example.healthcheck.model.weight.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.healthcheck.model.weight.entities.Weight

@Entity(
    tableName = "weight"
)
data class WeightDbEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    var weight : Float,
    var date : String,
) {

    fun toWeight() : Weight = Weight(
        weight = weight,
        date = date,
    )

    companion object {

        fun fromWeight(weight: Weight) : WeightDbEntity = WeightDbEntity(
            id = 0,
            weight = weight.weight,
            date = weight.date,
        )

    }

}