package com.example.healthcheck.model.medicines.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.healthcheck.model.medicines.service.MedicinesNotificationService


class MedicinesBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val service = MedicinesNotificationService(context)
            service.repairNotifications()
            Log.d("Notification", "Receiver восстановил уведомления для таблеток")
        }

    }

}