package com.example.tasklist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tasklist.R
import com.example.tasklist.data.Task
import com.example.tasklist.data.TaskDAO

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val taskDAO = TaskDAO(this)
        taskDAO.getRecordById(7)



    }
}