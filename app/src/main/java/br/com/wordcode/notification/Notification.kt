package br.com.wordcode.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput

class Notification(private val context: Context) {

    companion object {

        const val CHANNEL_ID = "br.com.wordcode.notification"
        const val NOTIFICATION_ID = 1010
        const val REPLY_KEY = "reply_action"

    }

    fun create(title: String, message: String) {

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var builder: NotificationCompat.Builder
        builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                CHANNEL_ID, "channel_name", NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationManager.createNotificationChannel(notificationChannel)

            NotificationCompat.Builder(context, notificationChannel.id) // android >= 26

        } else {
            NotificationCompat.Builder(context) //android <= 25
        }

        builder = builder
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    android.R.drawable.ic_dialog_info
                )
            )
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val replyRemote = RemoteInput.Builder(REPLY_KEY).run {
            setLabel("Insert your message here")
            build()
        }

        val replyAction = NotificationCompat.Action.Builder(
            0, "Reply", pendingIntent
        ).addRemoteInput(replyRemote)
            .build()

        builder.addAction(replyAction)


        notificationManager.notify(NOTIFICATION_ID, builder.build())

    }

}