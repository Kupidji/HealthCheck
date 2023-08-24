package com.example.domain.repository

interface SettingsStorageRepository {

    suspend fun setNutritionVisibility(visibility : Boolean)

    suspend fun getNutritionVisibility() : Boolean

    suspend fun setApplicationTheme(theme : Int)

    suspend fun getApplicationTheme() : Int

    suspend fun setFirstLaunchCompleted(completed : Boolean)

    suspend fun getFirstLaunchCompleted() : Boolean

    suspend fun setTimeOfReminderNotification(time : Long)

    suspend fun getTimeOfReminderNotification() : Long

    suspend fun getCurrentDate() : Long

    suspend fun setCurrentDate(date : Long)

}