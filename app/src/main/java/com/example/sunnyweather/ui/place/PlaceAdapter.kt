package com.example.sunnyweather.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.databinding.PlaceItemBinding
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.ui.weather.WeatherActivity

/**
 * Created by deqiangchen on 2024/10/22.
 */
class PlaceAdapter(private val fragment: PlaceFragment,private val placeList:List<Place>) :
RecyclerView.Adapter<PlaceAdapter.ViewHolder>(){

    inner class ViewHolder(binding: PlaceItemBinding) :RecyclerView.ViewHolder(binding.root){
        val placeName :TextView= binding.placeName
        val placeAddress :TextView= binding.placeAddress
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PlaceItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val viewHolder = ViewHolder(binding)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val place =placeList[position]
            val activity =fragment.activity
            if (activity is WeatherActivity){
                activity.draerLayout.closeDrawers()
                activity.viewModel.locationLng = place.location.lng
                activity.viewModel.locationLat =place.location.lat
                activity.viewModel.placeName =place.name
                activity.refreshWeather()
            }else{
                val intent = Intent(parent.context,WeatherActivity::class.java).apply {
                    putExtra("location_lng",place.location.lng)
                    putExtra("location_lat",place.location.lat)
                    putExtra("place_name",place.name)
                }
                fragment.startActivity(intent)
                fragment.activity?.finish()
            }
            fragment.viewModel.savePlace(place)
        }
        return viewHolder
    }

    override fun getItemCount()=placeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.setText(place.name)
        holder.placeAddress.setText(place.address)
    }
}