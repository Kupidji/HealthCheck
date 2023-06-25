package com.example.healthcheck.model.medicines.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.healthcheck.model.medicines.service.MedicinesNotificationService


class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("Notification", "Receiver Working!")
            val service = MedicinesNotificationService(context)
            service.repairNotifications()
        }

    }

}