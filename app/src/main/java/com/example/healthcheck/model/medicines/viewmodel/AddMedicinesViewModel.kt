package com.example.healthcheck.model.medicines.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.model.medicines.entities.Medicines
import com.example.healthcheck.model.medicines.retrofit.MedicinesRetrofitApi
import com.example.healthcheck.model.medicines.service.MedicinesNotificationService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddMedicinesViewModel(application: Application) : AndroidViewModel(application) {

    private var context = getApplication<Application>().applicationContext
    private var medicinesNotificationService = MedicinesNotificationService(context)

    private val interceptor = HttpLoggingInterceptor()

    val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://176.57.218.21:81")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var MedicineRetrofitApi = retrofit.create(MedicinesRetrofitApi::class.java)

    init {
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        MedicineRetrofitApi = retrofit.create(MedicinesRetrofitApi::class.java)
    }

    fun createNotification(timeInMillis : Long, title : String, channelID : Int) {
        if (timeInMillis != 0L) {
            medicinesNotificationService.setRepetitiveAlarm(timeInMillis, title, channelID)
        }
    }

    suspend fun createMedicine(medicines: Medicines) {
        Repositories.medicinesRepository.createMedicine(medicines)
    }


}