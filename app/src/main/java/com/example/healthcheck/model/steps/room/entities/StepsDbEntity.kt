package com.example.healthcheck.model.steps.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.healthcheck.model.steps.entities.Steps

@Entity (
    tableName = "steps"
)
data class StepsDbEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    @ColumnInfo(name = "count_of_steps")
    var countOfSteps : Int,
    var date : String,
) {

    fun toSteps() : Steps = Steps(
        countOfSteps = countOfSteps,
        date = date,
    )

    companion object {

        fun fromSteps(steps: Steps) : StepsDbEntity = StepsDbEntity (
            id = 0,
            countOfSteps = steps.countOfSteps,
            date = steps.date
        )

    }

}
