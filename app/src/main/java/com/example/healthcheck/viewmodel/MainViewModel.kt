package com.example.healthcheck.viewmodel

import androidx.lifecycle.ViewModel
import com.example.healthcheck.mainFragment1
import com.example.healthcheck.mainFragment2
import com.example.healthcheck.mainFragment3
import com.example.healthcheck.view.mainFragment

class MainViewModel : ViewModel() {

    val fragList = listOf(
        mainFragment1.newInstance(),
        mainFragment2.newInstance(),
        mainFragment3.newInstance(),
    )

}