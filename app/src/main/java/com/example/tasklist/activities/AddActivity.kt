package com.example.tasklist.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.tasklist.R
import com.example.tasklist.data.Task
import com.example.tasklist.data.TaskDAO
import com.example.tasklist.databinding.ActivityAddBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding

    private lateinit var taskDAO: TaskDAO

    private var dateMax: LocalDateTime? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskDAO = TaskDAO(this)

        binding.saveButton.setOnClickListener {
            val taskName = binding.nameEditText.text.toString()
            val dateFormat = dateMax!!.format(DateTimeFormatter.ISO_DATE_TIME)
            val task = Task(-1, taskName,false,dateFormat)
            taskDAO.insertTask(task)
            Toast.makeText(this, "Tarea guardad correctamente", Toast.LENGTH_SHORT).show()
            finish()
        }
        binding.selectDateMaxButton.setOnClickListener {
            showDatePickerDialog()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePickerDialog() {
        val now = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            showTimePickerDialog(year, month, dayOfMonth)
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showTimePickerDialog(year: Int, month: Int, dayOfMonth: Int) {
        val now = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
            dateMax = LocalDateTime.of(year, month + 1, dayOfMonth, hourOfDay, minute)
            binding.dateMaxSelectedTextView.text = dateMax?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true)
        timePickerDialog.show()
    }
}