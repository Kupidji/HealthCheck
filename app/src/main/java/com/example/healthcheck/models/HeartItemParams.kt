package com.example.healthcheck.models

data class HeartItemParams (
    val pressureUp : Int,
    val pressureDown : Int,
    val pulse : Int,
    val date : Long,
)
