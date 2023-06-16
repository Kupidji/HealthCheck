package com.example.healthcheck.model.medicines.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.healthcheck.R
import com.example.healthcheck.model.medicines.service.MedicinesNotificationService
import com.example.healthcheck.view.MainActivity


class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("Notification", "Receiver Working!")
            val toast = Toast.makeText(
                context.applicationContext,
                "bootReceiver",
                Toast.LENGTH_LONG
            )
            toast.show()
            val service = MedicinesNotificationService(context)
            service.repairNotifications()
            //context.startService(Intent(context, MedicinesNotificationService::class.java))
        }

    }

}