package com.singorenko.climate.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.singorenko.climate.BuildConfig
import com.singorenko.climate.R
import com.singorenko.climate.network.model.Constants
import com.singorenko.climate.network.model.WeatherRequestModel
import com.singorenko.climate.network.remote.utilities.ApiUtils
import com.singorenko.climate.view.adapter.ForecastRecyclerViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_city_layout.*

class MainCityFragment : Fragment() {

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_city, container, false)
    }


    private fun fillRecyclerView(weatherRequestModel: WeatherRequestModel){
//        recyclerViewForecast.isScrollContainer = false
        recyclerViewForecast.setHasFixedSize(true)
        recyclerViewForecast.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
//        recyclerViewForecast.isClickable = false
//        recyclerViewForecast.isEnabled = false
        recyclerViewForecast.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerViewForecast.adapter = ForecastRecyclerViewAdapter(weatherRequestModel)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MainCityFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onResume() {
        super.onResume()
        weatherRequest("Moscow")
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    private fun weatherRequest(weatherCity: String){
        disposable = ApiUtils.getApiService().getWeatherRequest(BuildConfig.apiKey, weatherCity, Constants.forecastDays)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({result ->
                refreshScreen(result)
                Log.d("TAG", "called successfully")
                Log.d("TAG", "called successfully"+result.current.temp_c)
                fillRecyclerView(result)
            },
                {error ->
                    Log.d("TAG","error on call: "+error.message)})
    }

    private fun refreshScreen(weatherCity: WeatherRequestModel){
        tv_main_city_temp.text = (weatherCity.current.temp_c.toString() + " C")
        tv_main_city_condition.text = (weatherCity.current.condition.text)
        tv_main_city_name.text = (weatherCity.location.name)

        var urlImage: String = (weatherCity.current.condition.icon).replace("\\/","///")
        urlImage = (urlImage.substring(2, urlImage.length))
//
//        Glide.with(this.context!!).load("https://$urlImage")
//            .centerCrop()
////            .placeholder(R.drawable.)
//            .into(iv_main_city_image_background)


        var urlImageTomorrow: String = (weatherCity.forecast.forecastday[0].day.condition.icon).replace("\\/","///")
        urlImageTomorrow = (urlImageTomorrow.substring(2, urlImageTomorrow.length))


//        Glide.with(this.context!!).load("https://$urlImageTomorrow")
//            .centerCrop().into(iv_condition_forecast_tomorrow)
//

    }
}
