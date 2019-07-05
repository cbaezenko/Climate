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
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.singorenko.climate.UploadWorker
import java.util.concurrent.TimeUnit
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import com.singorenko.climate.MyService
import android.content.Intent




class MainCityFragment : Fragment() {

    private lateinit var binding: MainCityLayoutBinding
    private var disposable: Disposable? = null

    private val uploadWorkRequest: UploadWorker? = null

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

        val startIntent = Intent(context, MyService::class.java)
        startIntent.action = "com.singorenko.climate.action_name"
        context!!.startService(startIntent)

//        val periodicWorkRequest = PeriodicWorkRequest.Builder(UploadWorker::class.java,
//            10,
//            TimeUnit.SECONDS, 5, TimeUnit.SECONDS).build()
//
//        WorkManager.getInstance().enqueue(periodicWorkRequest)

//        WorkManager.getInstance().enqueue(PeriodicWorkRequest.Builder(UploadWorker::class.java,
//            16, TimeUnit.MINUTES).build())

        WorkManager.getInstance().enqueue(PeriodicWorkRequest.Builder(UploadWorker::class.java,
            PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS).build())

//        if (Helper.isAppRunning(this!!.context!!, "com.facebook.orca")) {
//            // App is running
//            Log.d("TAG","com.facebook.messenger is running !!!!")
//        } else {
//            // App is not running
//            Log.d("TAG","com.facebook.messenger is NOT running !!!!")
//        }
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

//    object Helper {
//        fun isAppRunning(context: Context, packageName: String): Boolean {
//            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//            val procInfos: List<ActivityManager.RunningAppProcessInfo> = activityManager.runningAppProcesses
//            if (procInfos != null) {
//                for (processInfo in procInfos) {
//                    Log.d("TAG", "la aplicacion que esta funcionando es "+
//                            processInfo.processName)
//                    if (processInfo.processName == packageName) {
//                        return true
//                    }
//                }
//            }
//            return false
//        }
//    }
}
