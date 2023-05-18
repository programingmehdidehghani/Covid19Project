package com.example.covid19project.worker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.covid19project.R
import com.example.covid19project.model.StateResponse
import com.example.covid19project.repository.CovidIndiaRepository
import com.example.covid19project.ui.main.MainActivity
import com.example.covid19project.util.State
import com.example.covid19project.util.getPeriod
import com.example.covid19project.util.toDateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.sql.Date

class NotificationWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {


    @SuppressLint("UnspecifiedImmutableFlag", "NotificationPermission")
    private fun showNotification(totalCount: String, time: String) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context,0,intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = context.getString(R.string.default_notification_channel_id)
        val channelName = context.getString(R.string.default_notification_channel_name)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context,channelId)
            .setColor(ContextCompat.getColor(context,R.color.color_confirmed))
            .setSmallIcon(R.drawable.ic_stat_notification_icon)
            .setContentTitle(context.getString(R.string.text_confirmed_cases,totalCount))
            .setContentText(context.getString(R.string.text_last_updated, time))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0,notificationBuilder.build())
    }

    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result = coroutineScope{

        val repository: CovidIndiaRepository = get()

        val result = withContext(Dispatchers.Default){
            repository.getData().toList()
        }.filterIsInstance<State.Success<StateResponse>>()

        if (result.isNullOrEmpty()){
            Result.retry()
        } else {
            val totalDetails = result[0].data.stateWiseDetails[0]

            showNotification(
                totalDetails.confirmed,
                getPeriod(
                    totalDetails.lastUpdatedTime.toDateFormat() as Date
                )
            )

            Result.Success()
        }
    }

}