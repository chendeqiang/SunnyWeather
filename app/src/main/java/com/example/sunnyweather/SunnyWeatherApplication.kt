package com.example.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * Created by deqiangchen on 2024/10/21.
 */
class SunnyWeatherApplication :Application() {

    companion object{

        const val TOKEN = "83LBuPNc5orsI9B0"

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context= applicationContext
    }
}