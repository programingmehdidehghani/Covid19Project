package com.example.covid19project.worker

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.covid19project.R
import com.example.covid19project.ui.main.MainActivity
import org.koin.core.component.KoinComponent

class NotificationWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {


    @SuppressLint("UnspecifiedImmutableFlag")
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
    }

    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }

}