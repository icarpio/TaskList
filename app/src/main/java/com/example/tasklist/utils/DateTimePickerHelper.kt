package com.example.tasklist.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.util.Calendar

class DateTimePickerHelper (private val context: Context){

    @RequiresApi(Build.VERSION_CODES.O)
    fun showDatePickerDialog(onDateTimeSelected: (LocalDateTime) -> Unit) {
        val now = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(context, { _, year, month, dayOfMonth ->
            showTimePickerDialog(year, month, dayOfMonth, onDateTimeSelected)
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showTimePickerDialog(year: Int, month: Int, dayOfMonth: Int, onDateTimeSelected: (LocalDateTime) -> Unit) {
        val now = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(context, { _, hourOfDay, minute ->
            val dateMax = LocalDateTime.of(year, month + 1, dayOfMonth, hourOfDay, minute)
            onDateTimeSelected(dateMax)
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true)
        timePickerDialog.show()
    }
}