package com.singorenko.climate

import android.app.ActivityManager
import android.content.Context
import android.util.Log
import androidx.work.*
import java.util.concurrent.TimeUnit

class UploadWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
         try {
            val inputData: Data = myMethod()

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

            PeriodicWorkRequest.Builder(UploadWorker::class.java,
                PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.LINEAR,
                    OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS)
                .build()

            return Result.success()
        } catch (e: Exception) {
            Log.d("TAG", "error happen !"+e.message)
            return Result.failure()
        }
    }

    fun myMethod() : Data {
        var stringForFacebook = "Facebook is NOT ACTIVE"
        if(isAppRunning(applicationContext, "com.facebook.katana")
            || isAppRunning(applicationContext, "com.facebook.orca")
            || isAppRunning(applicationContext, "com.example.facebook")
            || isAppRunning(applicationContext, "com.facebook.android")) {
            stringForFacebook = "Facebook is OPEN!!!!"
        }

        Log.d("TAG", "running the method with a Work Manager!!!! $stringForFacebook")
        return Data.Builder().putString("KEY STRING","inputData").build()
    }

    fun isAppRunning (context: Context, packageName: String): Boolean {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val procInfos: List<ActivityManager.RunningAppProcessInfo> = activityManager.runningAppProcesses
            if (procInfos != null) {
                for (processInfo in procInfos) {
                    Log.d("TAG", "the application now in screen is "+
                            processInfo.processName)
                    if (processInfo.processName == packageName) {
                        return true
                    }
                }
            }
            return false
    }
}