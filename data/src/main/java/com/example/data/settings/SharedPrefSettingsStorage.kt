package com.example.data.settings

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.data.CHOOSEN_THEME
import com.example.data.CURRENT_DAY
import com.example.data.FIRST_LAUNCH
import com.example.data.HEALTHY_EAT_VISIBILITY
import com.example.data.SETTINGS
import com.example.data.TIME_OF_REGULAR
import com.example.domain.repository.SettingsStorageRepository
import java.util.Calendar

class SharedPrefSettingsStorage(private val context: Context) : SettingsStorageRepository {

    val storage = context.applicationContext.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
    val storageEdit = storage.edit()

    override suspend fun setNutritionVisibility(visibility: Boolean) {
        storageEdit.putBoolean(HEALTHY_EAT_VISIBILITY, visibility).apply()
    }

    override suspend fun getNutritionVisibility(): Boolean {
        return storage.getBoolean(HEALTHY_EAT_VISIBILITY, true)
    }

    override suspend fun setApplicationTheme(theme: Int) {
        storageEdit.putInt(CHOOSEN_THEME, theme).apply()
    }

    override suspend fun getApplicationTheme(): Int {
        return storage.getInt(CHOOSEN_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    override suspend fun setFirstLaunchCompleted(completed: Boolean) {
        storageEdit.putBoolean(FIRST_LAUNCH, completed).apply()
    }

    override suspend fun getFirstLaunchCompleted(): Boolean {
        return storage.getBoolean(FIRST_LAUNCH, true)
    }

    override suspend fun setTimeOfReminderNotification(time: Long) {
        storageEdit.putLong(TIME_OF_REGULAR, time).apply()
    }

    override suspend fun getTimeOfReminderNotification(): Long {
        return storage.getLong(TIME_OF_REGULAR, 0L)
    }

    override suspend fun getCurrentDate(): Long {
        return storage.getLong(CURRENT_DAY, Calendar.getInstance().timeInMillis)
    }

    override suspend fun setCurrentDate(date: Long) {
        storageEdit.putLong(CURRENT_DAY, date).apply()
    }

}