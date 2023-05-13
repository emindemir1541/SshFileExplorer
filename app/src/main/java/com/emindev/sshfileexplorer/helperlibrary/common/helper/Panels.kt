package com.emindev.expensetodolist.helperlibrary.common.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings

class Panels(private val context: Context) {

    @SuppressLint("QueryPermissionsNeeded")
    fun gps() {
        val gpsOpenPanel = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(gpsOpenPanel)
    }

    fun internet() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val panelIntent = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
            context.startActivity(panelIntent)
        }
    }

    fun volume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val panelIntent = Intent(Settings.Panel.ACTION_VOLUME)
            context.startActivity(panelIntent)
        }
    }

    fun nfc() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val panelIntent = Intent(Settings.Panel.ACTION_NFC)
            context.startActivity(panelIntent)
        }
    }

}