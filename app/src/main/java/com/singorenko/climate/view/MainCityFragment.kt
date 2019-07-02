package com.singorenko.climate.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.singorenko.climate.BuildConfig
import com.singorenko.climate.databinding.MainCityLayoutBinding
import com.singorenko.climate.network.model.Constants
import com.singorenko.climate.network.model.WeatherRequestModel
import com.singorenko.climate.network.remote.utilities.ApiUtils
import com.singorenko.climate.view.adapter.ForecastRecyclerViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainCityFragment : Fragment() {

    private lateinit var binding: MainCityLayoutBinding
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainCityLayoutBinding.inflate(inflater, container, false)
        return  binding.root
    }

    private fun fillRecyclerView(weatherRequestModel: WeatherRequestModel) {
        binding.apply {
            binding.recyclerViewForecast.setHasFixedSize(true)
            binding.recyclerViewForecast.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            binding.recyclerViewForecast.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            binding.recyclerViewForecast.adapter = ForecastRecyclerViewAdapter(weatherRequestModel)
        }
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

    //TODO: Esto debe sacarse de la actividad
    private fun weatherRequest(weatherCity: String) {
        disposable = ApiUtils.getApiService().getWeatherRequest(BuildConfig.apiKey, weatherCity, Constants.forecastDays)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                refreshScreen(result)
                Log.d("TAG", "called successfully")
                fillRecyclerView(result)
            },
                { error ->
                    Log.d("TAG", "error on call: " + error.message)
                })
    }

    private fun refreshScreen(weatherCity: WeatherRequestModel) {
        binding.apply {
            binding.tvMainCityTemp.text = (weatherCity.current.temp_c.toString() + " C")
            binding.tvMainCityCondition.text = (weatherCity.current.condition.text)
            binding.tvMainCityName.text = (weatherCity.location.name)
        }
    }
}
