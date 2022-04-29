package com.example.personalorganiser

import android.app.*
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.text.format.DateFormat
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.personalorganiser.databinding.FragmentGalleryBinding
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class Schedule : AppCompatActivity()
{
    private lateinit var binding: FragmentGalleryBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = FragmentGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()
        binding.submitSelection.setOnClickListener{scheduleNotification()}
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun  scheduleNotification()
    {
        val  intent = Intent(applicationContext, Notification::class.java)
        val title = binding.titleedittext.text.toString()
        val  message = binding.mesageedittext.tag.toString()
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notficationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT

        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        showAlert(time, title, message)
    }
    private fun showAlert (time: Long, title: String, message: String){
        val date = Date(time)
        val  dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        AlertDialog.Builder(this)
            .setTitle("Notification has been Scheduled")
            .setMessage("Title: "+ title + "\nMessage: "+message+"\nAt: "+dateFormat.format(date)+ " "+ timeFormat.format(time))
            .setPositiveButton("Done"){_,_ ->}
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getTime() : Long {
        val minute = binding.timeSelection.minute
        val hour = binding.timeSelection.hour
        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun  createNotificationChannel()
    {
        val name = "Schedule"
        val desc = "Schedule Notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val  channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val  notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}