package com.example.domain.repository

interface ProfileStorageRepository {

    fun getUserName() : String

    fun setUserName(name : String)

    fun getAge() : Int

    fun setAge(age : Int)

    fun getHeight() : Float

    fun setHeight(height : Float)

    fun getGender() : Boolean

    fun setGender(gender : Boolean)

}