package com.example.ui.components

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.data.local.RasiPalanDatabase
import com.example.data.model.RasiId
import com.example.data.repository.AstrologyDataProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/** Schedules the daily horoscope alert (inexact alarm, no special permission needed). */
object AlertScheduler {

    private const val REQUEST_CODE = 2001

    fun sync(context: Context, enabled: Boolean, hour: Int, minute: Int) {
        if (enabled) schedule(context, hour, minute) else cancel(context)
    }

    fun schedule(context: Context, hour: Int, minute: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val trigger = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) add(Calendar.DAY_OF_YEAR, 1)
        }.timeInMillis
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, trigger, pendingIntent(context))
    }

    fun cancel(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent(context))
    }

    private fun pendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, DailyAlertReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}

/** Fires at the chosen time, posts today's alert, and schedules the next day. */
class DailyAlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val pending = goAsync()
        val appContext = context.applicationContext
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dao = RasiPalanDatabase.getInstance(appContext).rasiDao()
                val profile = dao.getUserProfile().firstOrNull()
                val enabled = profile?.notificationsEnabled ?: true
                if (!enabled) return@launch

                // Schedule tomorrow's alert first so a failure below never breaks the chain.
                AlertScheduler.schedule(appContext, profile?.alertHour ?: 7, profile?.alertMinute ?: 0)

                val rasiId = RasiId.fromIndex(profile?.savedRasiId ?: 0)
                val rasi = AstrologyDataProvider.allRasis.first { it.id == rasiId }
                val today = Calendar.getInstance()
                val dateKey = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(today.time)
                val cached = dao.getCachedHoroscope("${rasiId.name}_$dateKey").firstOrNull()

                val luckyNumber: Int
                val luckyColor: String
                val general: String
                if (cached != null) {
                    luckyNumber = cached.luckyNumber
                    luckyColor = cached.luckyColorTamil
                    general = cached.generalTamil
                } else {
                    val item = AstrologyDataProvider.generateHoroscopeForRasi(rasiId, today)
                    luckyNumber = item.luckyNumber
                    luckyColor = item.luckyColorTamil
                    general = item.generalTamil
                }

                NotificationHelper.sendPersonalizedDailyNotification(
                    appContext,
                    rasi.nameTamil,
                    rasi.nameEnglish,
                    luckyNumber,
                    luckyColor,
                    general.take(120) + "..."
                )
            } finally {
                pending.finish()
            }
        }
    }
}

/** Alarms are cleared on reboot / app update, so re-schedule from the saved profile. */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action
        if (action != Intent.ACTION_BOOT_COMPLETED && action != Intent.ACTION_MY_PACKAGE_REPLACED) return
        val pending = goAsync()
        val appContext = context.applicationContext
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val profile = RasiPalanDatabase.getInstance(appContext).rasiDao().getUserProfile().firstOrNull()
                AlertScheduler.sync(
                    appContext,
                    profile?.notificationsEnabled ?: true,
                    profile?.alertHour ?: 7,
                    profile?.alertMinute ?: 0
                )
            } finally {
                pending.finish()
            }
        }
    }
}
