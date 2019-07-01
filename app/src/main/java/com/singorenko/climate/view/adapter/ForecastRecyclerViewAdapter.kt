package com.singorenko.climate.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.singorenko.climate.R
import com.singorenko.climate.network.model.WeatherRequestModel
import kotlinx.android.synthetic.main.main_city_layout.*

class ForecastRecyclerViewAdapter(private val weatherRequestModel: WeatherRequestModel): RecyclerView.Adapter<ForecastRecyclerViewAdapter.RecyclerViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        context = parent.context
        val layout = R.layout.item_forecast_weather
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(layout, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return weatherRequestModel.forecast.forecastday.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        val forecastFor = position + 1

        if(forecastFor<weatherRequestModel.forecast.forecastday.size) {

            val day: String = weatherRequestModel.forecast.forecastday[forecastFor].date
            val forecastTempMin: String = weatherRequestModel.forecast.forecastday[forecastFor].day.mintemp_c.toString()
            val forecastTempMax: String = weatherRequestModel.forecast.forecastday[forecastFor].day.maxtemp_c.toString()

            holder.tvForecastDay.text = day
            holder.tvForecastTempMin.text = forecastTempMin
            holder.tvForecastTempMax.text = forecastTempMax

            var urlImage: String =
                (weatherRequestModel.forecast.forecastday[position].day.condition.icon).replace("\\/", "///")
            urlImage = (urlImage.substring(2, urlImage.length))

            Glide.with(context).load("https://$urlImage")
                .centerCrop()
                .into(holder.ivForecastImage)
        }
    }


    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

       val tvForecastDay: TextView = itemView.findViewById(R.id.tv_forecast_day)
       val tvForecastTempMin: TextView = itemView.findViewById(R.id.tv_forecast_temp_min)
       val tvForecastTempMax: TextView = itemView.findViewById(R.id.tv_forecast_temp_max)
       val ivForecastImage: ImageView = itemView.findViewById(R.id.iv_forecast_image)
    }

}