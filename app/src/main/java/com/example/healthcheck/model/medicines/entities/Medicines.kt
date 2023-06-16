package com.example.healthcheck.model.medicines.entities


data class Medicines (
    val id : Long,
    var title : String,
    var dateStart : Long,
    var durationOfCourse : Int,
    var currentDayOfCourse : Int,
    var timeOfNotify1 : Long,
    var channelIDFirstTime : Int,
    var timeOfNotify2 : Long,
    var channelIDSecondTime : Int,
    var timeOfNotify3 : Long,
    var channelIDThirdTime : Int,
    var timeOfNotify4 : Long,
    var channelIDFourthTime : Int,
    var totalMissed : Int,
)