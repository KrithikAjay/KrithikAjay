package br.com.wordcode.notification

import android.app.NotificationManager
import android.app.RemoteInput
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        show_notification.setOnClickListener {

            Notification(this@MainActivity)
                .create("Notification Title", "Notification Message")

        }

        receiveReplyInput()
    }

    private fun receiveReplyInput() {

        val replyKey = Notification.REPLY_KEY
        val channelID = Notification.CHANNEL_ID

        val intent = this.intent

        val replyInput = RemoteInput.getResultsFromIntent(intent)

        if (replyInput != null) {
            val inputReplyString = replyInput.getCharSequence(replyKey).toString()

            show_notification.text = inputReplyString

            val notificationId = Notification.NOTIFICATION_ID

            val updateCurrentNotification =
                NotificationCompat.Builder(this@MainActivity, channelID)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            this.resources,
                            android.R.drawable.ic_dialog_info
                        )
                    )
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("Message sent success")
                    .setContentText("Updated notification")
                    .build()

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.notify(notificationId, updateCurrentNotification)

        }

    }
}
