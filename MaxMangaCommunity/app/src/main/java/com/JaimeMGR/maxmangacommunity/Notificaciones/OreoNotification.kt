package com.JaimeMGR.maxmangacommunity.Notificaciones

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.os.Build

class OreoNotification (base : Context) : ContextWrapper(base) {

    private var notificationManager: NotificationManager? = null

    companion object {
        private const val CHANNEL_ID = "com.JaimeMGR.maxmangacommunity"
        private const val CHANNEL_NAME = "MaxManga Community"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CrearCanal()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun CrearCanal() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.enableLights(false)
        channel.enableVibration(true)
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        getManager!!.createNotificationChannel(channel)
    }

    val getManager: NotificationManager?
        get() {
            if (notificationManager == null) {
                notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            return notificationManager
        }

    @TargetApi(Build.VERSION_CODES.O)
    fun getOreoNotification(
        titulo: String?,
        cuerpo: String?,
        pendingIntent: PendingIntent?,
        sonidoUri: Uri?,
        icono: String?
    ): Notification.Builder {

        return Notification.Builder(applicationContext, CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setContentTitle(titulo)
            .setContentText(cuerpo)
            .setSmallIcon(icono!!.toInt())
            .setSound(sonidoUri)
            .setAutoCancel(true)

    }

}