package com.example.sunnyweather.ui.weather


import android.content.Context
import android.hardware.input.InputManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.R
import com.example.sunnyweather.databinding.ActivityWeatherBinding
import com.example.sunnyweather.logic.model.Weather
import com.sunnyweather.android.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding

    lateinit var draerLayout :DrawerLayout

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding =ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        draerLayout = binding.drawerLayout

        if (viewModel.locationLng.isEmpty()){
            viewModel.locationLng = intent.getStringExtra("location_lng")?:""
        }
        if (viewModel.locationLat.isEmpty()){
            viewModel.locationLat = intent.getStringExtra("location_lat")?:""
        }
        if (viewModel.placeName.isEmpty()){
            viewModel.placeName = intent.getStringExtra("place_name")?:""
        }

        viewModel.weatherLiveData.observe(this, Observer { result->
            val weather = result.getOrNull()
            if (weather!=null){
                showWeatherInfo(weather)
            }else{
                Toast.makeText(this,"无法成功获取天气信息",Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            binding.swipeRefresh.isRefreshing =false
        })
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        refreshWeather()
        binding.swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }
        viewModel.refreshWeather(viewModel.locationLng,viewModel.locationLat)

        binding.now.navBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.drawerLayout.addDrawerListener(object :DrawerListener{
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerOpened(drawerView: View) {

            }

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
            }

            override fun onDrawerStateChanged(newState: Int) {

            }
        })
    }

    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng,viewModel.locationLat)
        binding.swipeRefresh.isRefreshing=true
    }

    private fun showWeatherInfo(weather: Weather) {
        binding.now.placeName.setText(viewModel.placeName)
        val realtime =weather.realtime
        val daily = weather.daily
        //填充now.xml布局数据
        val currentTempText = "${realtime.temperature.toInt()}°C"
        binding.now.currentTemp.setText(currentTempText)
        binding.now.currentSky.setText(getSky(realtime.skycon).info)
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        binding.now.currentAQI.setText(currentPM25Text)
        binding.now.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)

        //填充forecast.xml数据
        binding.forecast.forecastLayout.removeAllViews()
        val days=daily.skycon.size
        for (i in 0 until days){
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item,binding.forecast.forecastLayout,false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.setText(simpleDateFormat.format(skycon.date))
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.setText(sky.info)
            val tempText ="${temperature.min.toInt()} ~ ${temperature.max.toInt()} °C"
            temperatureInfo.setText(tempText)
            binding.forecast.forecastLayout.addView(view)
        }
        //填充life_index.xml数据
        val lifeIndex = daily.lifeIndex
        binding.lifeIndex.coldRiskText.setText(lifeIndex.coldRisk[0].desc)
        binding.lifeIndex.dressingText.setText(lifeIndex.dressing[0].desc)
        binding.lifeIndex.ultravioletText.setText(lifeIndex.ultraviolet[0].desc)
        binding.lifeIndex.carWashingText.setText(lifeIndex.carWashing[0].desc)
        binding.weatherLayout.visibility=View.VISIBLE
    }
}