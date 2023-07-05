package com.example.healthcheck.model.medicines.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MedicinesRetrofitApi {

    @POST("/search")
    fun getMedicinesRetrofit(@Body title : MedicineSendRequestRetrofit) : Call<MedicinesEntityRetrofit>

}