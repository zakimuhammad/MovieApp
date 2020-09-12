package com.example.mymovieapp.alarm

import android.content.Context
import android.content.SharedPreferences

class AlarmPreference(context: Context) {
    private val prefName = "AlarmPreference"
    private val dailyReminder = "daily_reminder"
    private val releaseReminder = "release_reminder"

    private val preference: SharedPreferences =
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    private val editor = preference.edit()

    fun setDailyReminderPreference(isActive: Boolean) {
        editor?.putBoolean(dailyReminder, isActive)
        editor?.apply()
    }

    fun setReleaseReminderPreference(isActive: Boolean) {
        editor?.putBoolean(releaseReminder, isActive)
        editor?.apply()
    }

    fun getDailyReminderPreference(): Boolean {
        return preference.getBoolean(dailyReminder, false)
    }

    fun getReleaseReminderPreference(): Boolean {
        return preference.getBoolean(releaseReminder, false)
    }
}