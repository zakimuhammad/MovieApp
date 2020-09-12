package com.example.mymovieapp.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.mymovieapp.BuildConfig
import com.example.mymovieapp.R
import com.example.mymovieapp.model.movies.MovieResponseModel
import com.example.mymovieapp.network.ApiMain
import com.example.mymovieapp.ui.release.ReleaseActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ReleaseReminderReceiver : BroadcastReceiver() {
    private val idReleaseReminderReceiver = 16

    override fun onReceive(context: Context, intent: Intent) {
        getReleaseMovieData(context)
    }

    private fun getReleaseMovieData(context: Context) {
        val date = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = dateFormat.format(date)

        ApiMain().services.getReleaseMovie(BuildConfig.API_KEY, today, today)
            .enqueue(object : Callback<MovieResponseModel> {
                override fun onFailure(call: Call<MovieResponseModel>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<MovieResponseModel>,
                    response: Response<MovieResponseModel>
                ) {
                    showNotification(context, response.body())
                }
            })
    }

    private fun showNotification(context: Context, movieModel: MovieResponseModel?) {
        val channelId = "Channel_16"
        val channelName = "ReleaseReminderReceiver Channel"
        val groupKey = "Movie Group"
        val maxNotification = 2

        val intentToRelease = Intent(context, ReleaseActivity::class.java)
        intentToRelease.putExtra("movie", movieModel)
        val pendingIntent = PendingIntent.getActivity(context, 1, intentToRelease, 0)

        val builder = NotificationCompat.Builder(context, channelId)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_movie)
            .setAutoCancel(true)

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (movieModel?.results != null) {
            if (movieModel.results.size < maxNotification) {
                val bigTextStyle = NotificationCompat.BigTextStyle()

                for (i in 0 until movieModel.results.size) {
                    bigTextStyle.bigText(movieModel.results[i]?.overview)
                    builder.setContentTitle(movieModel.results[i]?.title)
                }

                builder.setContentText("Overview")
                    .setStyle(bigTextStyle)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            context.resources,
                            R.drawable.movie_widget
                        )
                    )
            } else {
                val inboxStyle = NotificationCompat.InboxStyle()
                    .setBigContentTitle("${movieModel.results.size} new movies")

                for (i in 0 until 6) {
                    inboxStyle.addLine(movieModel.results[i]?.title)
                }
                inboxStyle.addLine("${movieModel.results.size - 6} more movies")

                builder.setContentTitle("${movieModel.results.size} new movies")
                    .setContentText("Movies")
                    .setGroup(groupKey)
                    .setStyle(inboxStyle)
            }

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                builder.setChannelId(channelId)
                notificationManagerCompat.createNotificationChannel(channel)
            }

            notificationManagerCompat.notify(idReleaseReminderReceiver, builder.build())
        }
    }

    fun setReleaseReminderReceiver(context: Context, isActive: Boolean) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val now = Calendar.getInstance()
        var time = calendar.timeInMillis
        if (calendar.before(now)) {
            time += 24 * 60 * 60 * 1000
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReleaseReminderReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, idReleaseReminderReceiver, intent, 0)

        if (isActive) {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                time,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } else {
            alarmManager.cancel(pendingIntent)
        }
    }
}