package com.example.healthcheck.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.usecase.profile.GetAge
import com.example.domain.usecase.profile.GetGender
import com.example.domain.usecase.profile.GetHeight
import com.example.domain.usecase.profile.GetUserName
import com.example.domain.usecase.profile.SetAge
import com.example.domain.usecase.profile.SetGender
import com.example.domain.usecase.profile.SetHeight
import com.example.domain.usecase.profile.SetUserName
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val _userName = MutableSharedFlow<String>(1, 0 ,BufferOverflow.DROP_OLDEST)
    val userName = _userName.asSharedFlow()

    private val _age = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val age = _age.asSharedFlow()

    private val _height = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val height = _height.asSharedFlow()

    private val _gender = MutableSharedFlow<Boolean>(1, 0, BufferOverflow.DROP_OLDEST)
    val gender = _gender.asSharedFlow()

    init {
        viewModelScope.launch(AppDispatchers.main) {
            val getUserName = GetUserName(repository = Repositories.profileStorage)
            _userName.emit(getUserName.execute())
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getAge = GetAge(repository = Repositories.profileStorage)
            _age.emit(getAge.execute())
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getHeight = GetHeight(repository = Repositories.profileStorage)
            _height.emit(getHeight.execute())
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getGender = GetGender(repository = Repositories.profileStorage)
            _gender.emit(getGender.execute())
        }

    }

    fun setUserName(name : String) {
        viewModelScope.launch(AppDispatchers.main) {
            val setUserName = SetUserName(repository = Repositories.profileStorage)
            setUserName.execute(name = name)
        }
    }

    fun setAge(age : Int) {
        viewModelScope.launch(AppDispatchers.main) {
            val setAge = SetAge(repository = Repositories.profileStorage)
            setAge.execute(age = age)
        }
    }

    fun setHeight(height : Float) {
        viewModelScope.launch(AppDispatchers.main) {
            val setHeight = SetHeight(repository = Repositories.profileStorage)
            setHeight.execute(height = height)
        }
    }

    fun setGender(gender : Boolean) {
        viewModelScope.launch(AppDispatchers.main) {
            val setGender = SetGender(repository = Repositories.profileStorage)
            setGender.execute(gender = gender)
        }
    }

}