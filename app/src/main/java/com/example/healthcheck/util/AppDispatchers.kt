package com.example.healthcheck.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

object AppDispatchers {
    val main : MainCoroutineDispatcher = Dispatchers.Main
    val io : CoroutineDispatcher = Dispatchers.IO
    val default : CoroutineDispatcher = Dispatchers.Default
    val unconfined : CoroutineDispatcher =  Dispatchers.Unconfined
}