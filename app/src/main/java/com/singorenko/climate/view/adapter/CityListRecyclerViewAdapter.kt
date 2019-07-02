package com.singorenko.climate.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.singorenko.climate.R
import com.singorenko.climate.network.model.WeatherRequestModel
import java.util.zip.Inflater

class CityListRecyclerViewAdapter(val cityList: MutableList<WeatherRequestModel>) : RecyclerView.Adapter<CityListRecyclerViewAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
    val layout = R.layout.item_city_weather
    val inflater = LayoutInflater.from(parent.context)
    val view = inflater.inflate(layout, parent, false)
    return RecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
    return cityList.size }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    }
}