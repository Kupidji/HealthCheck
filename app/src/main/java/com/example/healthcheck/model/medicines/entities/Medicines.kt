package com.example.healthcheck.model.medicines.entities

import java.time.LocalDate

data class Medicines (
    var title : String,
    var dateStart : Int,
    var durationOfCourse : Int,
    var timeOfNotify1 : Long,
    var timeOfNotify2 : Long,
    var timeOfNotify3 : Long,
    var timeOfNotify4 : Long,
) {


}