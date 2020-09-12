package com.example.mymovieapp.ui.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.example.mymovieapp.R
import com.example.mymovieapp.alarm.AlarmPreference
import com.example.mymovieapp.alarm.DailyReminderReceiver
import com.example.mymovieapp.alarm.ReleaseReminderReceiver
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : LocalizationActivity() {
    private var alarmPreference: AlarmPreference? = null
    private var dailyReminderReceiver: DailyReminderReceiver? = null
    private var releaseReminderReceiver: ReleaseReminderReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        dailyReminderReceiver =
            DailyReminderReceiver()
        releaseReminderReceiver =
            ReleaseReminderReceiver()
        alarmPreference = AlarmPreference(this)

        setSupportActionBar(toolbar_setting)

        if (alarmPreference!!.getDailyReminderPreference()) {
            switch_daily_reminder.isChecked = true
        }

        if (alarmPreference!!.getReleaseReminderPreference()) {
            switch_release_reminder.isChecked = true
        }

        btn_save.setOnClickListener {
            setAlarmPreference()
            finish()
        }

        btn_cancel.setOnClickListener {
            finish()
        }

        btn_language.setOnClickListener {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }

    }

    private fun setAlarmPreference() {
        alarmPreference?.setDailyReminderPreference(switch_daily_reminder.isChecked)
        dailyReminderReceiver?.setAlarmDailyReminder(this, switch_daily_reminder.isChecked)

        alarmPreference?.setReleaseReminderPreference(switch_release_reminder.isChecked)
        releaseReminderReceiver?.setReleaseReminderReceiver(this, switch_release_reminder.isChecked)
    }
}
