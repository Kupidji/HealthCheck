package com.example.healthcheck.model.heart.entities

data class Heart (
    var id : Int,
    var pressureUp : Int,
    var pressureDown : Int,
    var pulse : Int,
    var date : Long,
)