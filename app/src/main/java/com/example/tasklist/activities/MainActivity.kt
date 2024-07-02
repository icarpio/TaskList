package com.example.tasklist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.tasklist.adapters.TaskAdapter
import com.example.tasklist.data.Task
import com.example.tasklist.data.TaskDAO
import com.example.tasklist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskDAO: TaskDAO
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskList:List<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener datos desde SQLite
        taskDAO = TaskDAO(this)

        taskAdapter = TaskAdapter(){
            Toast.makeText(this,"Click en tarea: ${taskList[it].name}", Toast.LENGTH_SHORT).show()
        }
        // Configurar RecyclerView
        binding.recyclerView.adapter = taskAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }


    }
    override fun onResume(){
        super.onResume()
        taskList = taskDAO.getAllRecords()
        taskAdapter.updateData(taskList)

    }



}