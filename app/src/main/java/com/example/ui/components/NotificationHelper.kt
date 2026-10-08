package com.example.ui.components

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R

object NotificationHelper {

    private const val CHANNEL_ID = "rasi_palan_daily_alerts_om"
    private const val CHANNEL_NAME = "தினசரி ராசி பலன் (Om Sound Alerts)"
    private const val CHANNEL_DESC = "Personalized daily Rasi palan alerts with sacred Om sound"

    fun getOmSoundUri(context: Context): Uri {
        return Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/raw/om_chant")
    }

    fun playSacredOmSound(context: Context) {
        try {
            val mediaPlayer = MediaPlayer.create(context, R.raw.om_chant)
            mediaPlayer.setOnCompletionListener { mp -> mp.release() }
            mediaPlayer.start()
        } catch (e: Exception) {
            // Audio playback fallback
        }
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val soundUri = getOmSoundUri(context)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESC
                enableVibration(true)
                setSound(soundUri, audioAttributes)
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendPersonalizedDailyNotification(
        context: Context,
        rasiNameTamil: String,
        rasiNameEnglish: String,
        luckyNumber: Int,
        luckyColor: String,
        advice: String
    ) {
        createNotificationChannel(context)
        // The Om sound is played by the notification channel itself; playing it here too doubled it.

        val soundUri = getOmSoundUri(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            101,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = "🕉️ இன்றைய $rasiNameTamil ராசி பலன் | $rasiNameEnglish"
        val message = "அதிர்ஷ்ட எண்: $luckyNumber | நிறம்: $luckyColor\n$advice"

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_rasi)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setSound(soundUri)
            .setAutoCancel(true)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1001, notification)
    }

    fun sendAuspiciousTimingNotification(
        context: Context,
        nallaNeram: String,
        thithi: String
    ) {
        createNotificationChannel(context)
        // The Om sound is played by the notification channel itself; playing it here too doubled it.

        val soundUri = getOmSoundUri(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            102,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = "🕉️ இன்றைய நல்ல நேரம் நினைவூட்டல்"
        val message = "நல்ல நேரம்: $nallaNeram | $thithi\nசுப காரியங்களுக்கு உகந்த நேரம்!"

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_rasi)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setSound(soundUri)
            .setAutoCancel(true)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1002, notification)
    }
}
