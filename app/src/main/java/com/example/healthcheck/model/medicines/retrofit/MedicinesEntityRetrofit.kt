package com.example.healthcheck.model.medicines.retrofit

import com.google.gson.annotations.SerializedName

data class MedicinesEntityRetrofit (
    @SerializedName("text")
    val title : List<List<String>>
)

data class MedicineSendRequestRetrofit(
    @SerializedName("name")
    val title : String
)
