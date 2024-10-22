package com.example.sunnyweather.ui.place

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.databinding.PlaceItemBinding
import com.example.sunnyweather.logic.model.Place

/**
 * Created by deqiangchen on 2024/10/22.
 */
class PlaceAdapter(private val fragment: Fragment,private val placeList:List<Place>) :
RecyclerView.Adapter<PlaceAdapter.ViewHolder>(){

    inner class ViewHolder(binding: PlaceItemBinding) :RecyclerView.ViewHolder(binding.root){
        val placeName :TextView= binding.placeName
        val placeAddress :TextView= binding.placeAddress
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = PlaceItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount()=placeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.setText(place.name)
        holder.placeAddress.setText(place.address)
    }
}