package com.singorenko.climate

import android.graphics.Bitmap
import android.R
import android.app.*
import android.content.Context
import android.graphics.BitmapFactory
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.singorenko.climate.view.MainActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import android.R.id
import android.app.ActivityManager.RunningTaskInfo
import android.app.ActivityManager
//import sun.invoke.util.VerifyAccess.getPackageName


class MyService : Service() {
    private var disposable: Disposable? = null

    override fun onCreate() {
        super.onCreate()
        startServiceWithNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.action == "com.singorenko.climate.action_name") {
            startServiceWithNotification()
            Log.d("TAG_service", "el servicio es iniciado!")
            scan()
        } else
            Log.d("TAG_service", "el servicio NO es iniciado!")
        stopMyService()
        return START_STICKY
    }

    // In case the service is deleted or crashes some how
    override fun onDestroy() {
        isServiceRunning = false
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        // Used only in case of bound services.
        return null
    }


    fun scan (){
        var value = 0
        disposable = Observable
            .interval(3, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    isAppRunning(applicationContext, "com.facebook.katana")
                    secondCheckApp()
                    value++
                    Log.d("TAG_service", "called successfully")
                    Log.d("TAG_service", "called successfully"+thirdChange(applicationContext))
                    Log.d("TAG_service", "running on background!!!!$value")
                },
                { error ->
                    Log.d("TAG_service", "error on call: " + error.message)
                }
            )
    }

    internal fun startServiceWithNotification() {
        if (isServiceRunning) return
        isServiceRunning = true

        createNotificationChannel("my_channel_0101")

        val notificationIntent = Intent(getApplicationContext(), MainActivity::class.java)
        notificationIntent.setAction("com.singorenko.climate.action_name")  // A string containing the action name
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val contentPendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val icon = BitmapFactory.decodeResource(getResources(), R.drawable.alert_dark_frame)

        val notification = NotificationCompat.Builder(this)
            .setContentTitle("hola que hace")
            .setTicker("hola")
            .setContentText("texto")
            .setSmallIcon(R.drawable.alert_light_frame)
            .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
            .setContentIntent(contentPendingIntent)
            .setChannelId("my_channel_0101")
            .setOngoing(true)
            //                .setDeleteIntent(contentPendingIntent)  // if needed
            .build()
        notification.flags =
            notification.flags or Notification.FLAG_NO_CLEAR     // NO_CLEAR makes the notification stay when the user performs a "delete all" command
        startForeground(NOTIFICATION_ID, notification)
    }

    internal fun stopMyService() {
        stopForeground(true)
        stopSelf()
        isServiceRunning = false
    }

    private fun createNotificationChannel(channel_name: String) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = channel_name
            val descriptionText = channel_name
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channel_name, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        internal val NOTIFICATION_ID = 543

        var isServiceRunning = false
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

    fun secondCheckApp(){
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = activityManager.getRunningTasks(Integer.MAX_VALUE)

        for (i in tasks.indices) {
            Log.d("TAG_service", "Running task: " + tasks[i].baseActivity.toShortString() + "\t\t ID: " + tasks[i].id)
        }
    }

    fun thirdChange(context: Context) : String{
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTasks = activityManager.getRunningTasks(4096)
        return runningTasks.get(0).topActivity.getPackageName()
    }
}