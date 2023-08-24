package com.example.data.steps.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.steps.entities.StepsForDb


@Entity (
    tableName = "steps"
)
data class StepsDbEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    @ColumnInfo(name = "count_of_steps")
    var countOfSteps : Int,
    var date : Long,
) {

    fun toSteps() : StepsForDb = StepsForDb(
        id = id,
        countOfSteps = countOfSteps,
        date = date,
    )

    companion object {

        fun fromSteps(steps: StepsForDb) : StepsDbEntity = StepsDbEntity (
            id = 0,
            countOfSteps = steps.countOfSteps,
            date = steps.date
        )

        fun forUpdate(steps: StepsForDb) : StepsDbEntity = StepsDbEntity(
            id = steps.id,
            countOfSteps = steps.countOfSteps,
            date = steps.date
        )

    }

}
