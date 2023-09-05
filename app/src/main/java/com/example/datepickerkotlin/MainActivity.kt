package com.example.datepickerkotlin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.datepickerkotlin.databinding.ActivityMainBinding
import java.text.DateFormat
import java.time.Clock
import java.time.ZoneId
import java.util.Locale
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val meetingStart = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        val currentMoment: Long = Clock.system(ZoneId.of("Europe/Copenhagen")).millis()
        val t: Long = System.currentTimeMillis()
        Log.d("APPLE", "currentMoment unit time $currentMoment")
        Log.d("APPLE", "currentMoment unit time $t")

        val dateFormatter = DateFormat.getDateInstance(DateFormat.LONG, Locale.ENGLISH)
        val timeFormatter = DateFormat.getTimeInstance() // using device locale
        val dateString = dateFormatter.format(currentMoment)
        val timeString = timeFormatter.format(currentMoment)
        Log.d("APPLE", "currentMoment $dateString $timeString")

        binding.buttonDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val currentYear = calendar[Calendar.YEAR]
            val currentMonth = calendar[Calendar.MONTH]
            val currentDayOfMonth = calendar[Calendar.DATE]
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
                    meetingStart.set(Calendar.YEAR, year)
                    meetingStart.set(Calendar.MONTH, month)
                    meetingStart.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val dateFormatterLocal = DateFormat.getDateInstance()
                    val dateStringLocal = dateFormatterLocal.format(meetingStart.timeInMillis)
                    binding.buttonDate.text = dateStringLocal
                }
            val dialog = DatePickerDialog(
                this, dateSetListener, currentYear, currentMonth, currentDayOfMonth
            )
            dialog.show()
        }

        binding.buttonTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val currentHourOfDay = calendar[Calendar.HOUR_OF_DAY]
            val currentMinute = calendar[Calendar.MINUTE]
            val timeSetListener =
                TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->
                    meetingStart[Calendar.HOUR_OF_DAY] = hourOfDay
                    meetingStart[Calendar.MINUTE] = minute
                    meetingStart[Calendar.SECOND] = 0 // seconds not relevant
                    val timeFormatterLocal = DateFormat.getTimeInstance(DateFormat.SHORT)
                    val timeStringLocal = timeFormatterLocal.format(meetingStart.timeInMillis)
                    binding.buttonTime.text = timeStringLocal
                }
            val dialog = TimePickerDialog(
                this, timeSetListener, currentHourOfDay, currentMinute, true
            )
            dialog.show()
        }

        binding.showTimesButton.setOnClickListener {
            val dateFormatterLocal =
                DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG)
            val date = meetingStart.time
            val dateTimeString = dateFormatterLocal.format(date)
            binding.timeTextView.text = dateTimeString

            binding.unixTimeMiliSecondsTextView.text =
                "Unit time in milliseconds: ${meetingStart.timeInMillis}"

            val timeInSeconds = convertCalendarToTimeInSeconds(meetingStart)
            binding.unixTimeSecondsTextView.text = "Unix time in seconds: $timeInSeconds"
        }
    }

    private fun convertCalendarToTimeInSeconds(calendar: Calendar): Int {
        val timeInMillis: Long = calendar.timeInMillis
        return (timeInMillis / 1000.0).roundToInt()
    }
}