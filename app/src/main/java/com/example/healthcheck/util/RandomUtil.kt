package com.example.healthcheck.util

import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs

object RandomUtil {
    private val seed = AtomicInteger()
    fun getRandomInt() = abs(seed.getAndIncrement() + System.currentTimeMillis().toInt())
}