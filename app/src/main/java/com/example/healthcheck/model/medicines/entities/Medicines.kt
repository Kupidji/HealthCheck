package com.example.healthcheck.model.medicines.entities


data class Medicines (
    var title : String,
    var dateStart : String,
    var durationOfCourse : Int,
    var currentDayOfCourse : Int,
    var timeOfNotify1 : Long,
    var timeOfNotify2 : Long,
    var timeOfNotify3 : Long,
    var timeOfNotify4 : Long,
)