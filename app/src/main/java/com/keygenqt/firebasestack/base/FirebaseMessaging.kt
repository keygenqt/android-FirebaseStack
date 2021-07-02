package com.keygenqt.firebasestack.base

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.keygenqt.firebasestack.R
import com.keygenqt.firebasestack.ui.common.base.ActivityMain
import com.keygenqt.firebasestack.ui.user.components.NavScreenUser
import timber.log.Timber

class FirebaseMessaging : FirebaseMessagingService() {

    companion object {

        const val DEEP_LINK_URI = "https://keygenqt.com"

        private var ID: Int = 1

        fun getToken(delegate: (String) -> Unit) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    try {
                        throw task.exception!!
                    } catch (ex: Exception) {

                    }
                    return@OnCompleteListener
                } else {
                    delegate.invoke(task.result.toString())
                }
            })
        }
    }

    override fun onNewToken(token: String) {
        Timber.d("Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            sendNotification(it.channelId ?: "channelId", it.title ?: "", it.body ?: "", remoteMessage.data["uri"])
        }
    }

    private fun sendNotification(channelId: String, title: String, messageBody: String, uri: String?) {

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            (uri ?: "$DEEP_LINK_URI/${NavScreenUser.ChatList.route}").toUri(),
            this,
            ActivityMain::class.java
        )

        val deepLinkPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val groupBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_24)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setGroup(channelId)
            .setGroupSummary(true)
            .setContentIntent(deepLinkPendingIntent)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_24)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setGroup(channelId)
            .setContentIntent(deepLinkPendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, groupBuilder.build())
        notificationManager.notify(ID, notificationBuilder.build())
        ID++
    }
}