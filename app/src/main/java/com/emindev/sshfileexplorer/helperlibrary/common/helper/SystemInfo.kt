package com.emindev.expensetodolist.helperlibrary.common.helper

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.text.format.Formatter


@SuppressLint("HardwareIds")
object SystemInfo : Build() {

        var PACKAGE_NAME = ""

        fun packageName(context: Context) = context.packageName
        fun packageNameWithoutDat(): String {
            val wordList = PACKAGE_NAME.split(".")
            return wordList[0] + wordList[1] + wordList[2]
        }

        fun deviceId(context: Context) = android.provider.Settings.Secure.getString(context.contentResolver, android.provider.Settings.Secure.ANDROID_ID)
        private fun wifiManager(context: Context) = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        fun ipAddress(context: Context) = Formatter.formatIpAddress(wifiManager(context).connectionInfo.ipAddress)


        fun deviceList() = mapsToArrayList(device())
        fun androidList() = mapsToArrayList(android())
        fun appList() = mapsToArrayList(app())


        fun allList(context: Context): ArrayList<String> {
            val all = ArrayList<String>()
            all.add(packageName(context))
            all.add(deviceId(context))
            all.addAll(deviceList())
            all.addAll(androidList())
            all.addAll(appList())
            return all
        }

        private fun device(): MutableMap<String, String> {
            val device: MutableMap<String, String> = HashMap()
            device["brand"] = BRAND
            device["Model"] = MODEL
            device["ID"] = ID
            device["hardware"] = HARDWARE
            device["product"] = PRODUCT
            device["serial"] = SERIAL
            device["manufacture"] = MANUFACTURER
            device["board"] = BOARD
            device["host"] = HOST
            device["fingerprint"] = FINGERPRINT
            device["cpuAbi"] = CPU_ABI
            device["cpuAbi2"] = CPU_ABI2
            device["sku"] = SKU
            device["device"] = DEVICE
            device["bootloader"] = BOOTLOADER
            device["tags"] = TAGS

            return device
        }


        private fun android(): MutableMap<String, String> {
            val android: MutableMap<String, String> = HashMap()
            android["sdk"] = VERSION.SDK_INT.toString()
            android["user"] = USER
            android["userType"] = TYPE
            android["incremental"] = VERSION.INCREMENTAL
            android["version"] = VERSION.RELEASE
            android["host"] = HOST
            return android
        }

        private fun app(): MutableMap<String, String> {
            val app: MutableMap<String, String> = HashMap()
          //versionCode"] = BuildConfig.VERSION_CODE.toString()
           // app["versionName"] = BuildConfig.VERSION_NAME
            return app
        }

        private fun mapsToArrayList(map: MutableMap<String, String>): ArrayList<String> {
            val arrayList = ArrayList<String>()
            for (value in map) {
                arrayList.add("${value.key}: ${value.value}")
            }
            return arrayList
        }

}







