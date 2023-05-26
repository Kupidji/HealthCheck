package com.example.healthcheck.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.healthcheck.view.mainFragment1
import com.example.healthcheck.view.mainFragment2
import com.example.healthcheck.view.mainFragment3

class MainViewModel : ViewModel() {

    //lateinit var currentFrag : MutableLiveData<Int>

    val fragList = listOf(
        mainFragment1.newInstance(),
        mainFragment2.newInstance(),
        mainFragment3.newInstance(),
    )

    init {
        //currentFrag.value = 0
    }

}