package com.example.data.profile

import android.content.Context
import com.example.data.AGE
import com.example.data.GENDER
import com.example.data.HEIGHT
import com.example.data.NAME
import com.example.data.PROFILE
import com.example.domain.repository.ProfileStorageRepository

class SharedPrefProfileStorage(private val context : Context) : ProfileStorageRepository {

    val storage = context.applicationContext.getSharedPreferences(PROFILE, Context.MODE_PRIVATE)
    val storageEdit = storage.edit()

    override fun getUserName(): String {
        return storage.getString(NAME, "") ?: ""
    }

    override fun setUserName(name: String) {
        storageEdit.putString(NAME, name).apply()
    }

    override fun getAge(): Int {
        return storage.getInt(AGE, 10)
    }

    override fun setAge(age: Int) {
        storageEdit.putInt(AGE, age).apply()
    }

    override fun getHeight(): Float {
        return storage.getFloat(HEIGHT, 140F)
    }

    override fun setHeight(height: Float) {
        storageEdit.putFloat(HEIGHT, height).apply()
    }

    override fun getGender(): Boolean {
        return storage.getBoolean(GENDER, false)
    }

    override fun setGender(gender: Boolean) {
        storageEdit.putBoolean(GENDER, gender).apply()
    }
}