package com.example.healthcheck.util

import java.util.Calendar

object Constants {

    /////////////Уведомления

    //действие
    const val EXTRA_EXACT_ALARM_TIME = "EXTRA_EXACT_ALARM_TIME"
    const val ACTION_SET_EXACT = "ACTION_SET_EXTRA"
    const val ACTION_SET_REPETITIVE_EXACT = "ACTION_SET_REPETITIVE_EXACT"
    const val ACTION_SET_REPETITIVE_EXACT_REGULAR = "ACTION_SET_REPETITIVE_EXACT_REGULAR"
    const val CHANNEL_ID = "CHANNEL_ID"

    //medicines
    const val MEDICINES_CHANNEL = "MEDICINES_CHANNEL"
    const val MESSAGE = "MESSAGE"
    const val MEDICINE = "Medicine"

    //Напоминание о ежедневных действиях
    const val REGULAR_CHANNEL = "REGULAR_CHANNEL"
    const val REGULAR = "Reminder"
    const val REGULAR_MESSAGE = "Напоминание"
    const val REGULAR_CHANNEL_ID = 1232149812


    ///////////////////////

    /////////////sharedPreferences

    //Уведомления
    const val TIME_OF_NOTIFICATION = "TIME_OF_NOTIFICATION"

    //Настройки
    const val HEALTHY_EAT_VISIBILITY = "HEALTHY_EAT_VISIBILITY"

    //тема приложения
    const val CHOOSEN_THEME = "CHOOSEN_THEME"

    //Первый раз
    const val IS_FIRST_TIME = "IS_FIRST_TIME"

    //шаги
    const val STEPS = "STEPS"
    const val TARGET = "TARGET" //цель для шагов
    const val STEPS_PER_DAY = "STEPS_PER_DAY" //за день

    //таблетки
    const val FIRST_TIME = "FIRST_TIME"
    const val SECOND_TIME = "SECOND_TIME"
    const val THIRD_TIME = "THIRD_TIME"
    const val FOURTH_TIME = "FOURTH_TIME"
    const val LAST_UPDATE_DATE = "LAST_UPDATE_DATE"

    //сон
    const val SLEEP = "SLEEP"
    const val TIME_SLEEP = "TIME_SLEEP"

    //Вес
    const val WEIGHT = "WEIGHT"
    const val WEIGHT_FOR_DAY = "WEIGHT_FOR_DAY"
    const val NECK = "NECK"
    const val WAIST = "WAIST"
    const val FOREARM = "FOREARM"
    const val WRIST = "WRIST"
    const val HIPS = "HIPS"
    const val HIP_1 = "HIP_1"
    const val HIP_2 = "HIP_2"
    const val SHIN = "SHIN"
    const val FAT = "FAT"
    const val WEIGHT_FOR_WEEK = "WEIGHT_FOR_WEEK"

    //Start
    const val START = "START"
    const val FIO = "FIO"
    const val GENDER = "GENDER"
    const val AGE = "AGE"
    const val WEIGHT_START = "WEIGHT_START"
    const val HEIGHT_START = "HEIGHT_START"

    //Cardio
    const val CARDIO = "CARDIO"
    const val PRESSURE = "PRESSURE"
    const val UPPER = "UPPER"
    const val LOWER = "LOWER"
    const val PULSE = "PULSE"

    ////////////////////
}