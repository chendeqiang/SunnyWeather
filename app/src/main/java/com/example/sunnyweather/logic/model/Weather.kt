package com.example.sunnyweather.logic.model

/**
 * Created by deqiangchen on 2024/10/22.
 */
data class Weather(val realtime: RealtimeResponse.Realtime,val daily: DailyResponse.Daily)