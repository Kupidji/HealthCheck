package com.example.healthcheck.model.weight.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.healthcheck.model.steps.entities.Steps
import com.example.healthcheck.model.steps.room.entities.StepsDbEntity
import com.example.healthcheck.model.weight.entities.Weight

@Entity(
    tableName = "weight"
)
data class WeightDbEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    var weight : Float,
    var date : Long,
) {

    fun toWeight() : Weight = Weight(
        id = id,
        weight = weight,
        date = date,
    )

    companion object {

        fun fromWeight(weight: Weight) : WeightDbEntity = WeightDbEntity(
            id = 0,
            weight = weight.weight,
            date = weight.date,
        )

        fun forUpdateWeight(weight: Weight) : WeightDbEntity = WeightDbEntity(
            id = weight.id,
            weight = weight.weight,
            date = weight.date
        )

    }

}