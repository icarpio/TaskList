package com.example.tasklist.activities

import android.app.AlertDialog
import android.content.DialogInterface
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

        taskAdapter = TaskAdapter(emptyList(),{
            Toast.makeText(this,"Click en tarea: ${taskList[it].name}", Toast.LENGTH_SHORT).show()
        },{
            showDeleteConfirmationDialog(it)
            loadData()
        },{
            showDoneConfirmationDialog(it)
            loadData()
        })
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
        loadData()
    }

    private fun loadData() {
        taskList = taskDAO.getAllRecords()
        taskAdapter.updateData(taskList)
    }

    fun showDeleteConfirmationDialog(position: Int) {
        // Crea el AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Estás seguro de que deseas eliminar esta tarea?")
            .setPositiveButton("Sí") { dialog, id ->
                // Usuario hizo clic en "Sí", así que elimina la tarea
                taskDAO.deleteTask(taskList[position])
                Toast.makeText(
                    this,
                    "Tarea borrada correctamente: ${taskList[position].name}",
                    Toast.LENGTH_SHORT
                ).show()
                loadData()
            }
            .setNegativeButton("No") { dialog, id ->
                // Usuario hizo clic en "No", así que solo cierra el diálogo
                dialog.dismiss()
            }
        // Muestra el AlertDialog
        builder.create().show()
    }

    fun showDoneConfirmationDialog(position: Int) {
        val task = taskList[position]
        // Crea el AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Seguro que quieres modificar el estado de la tarea?")
            .setPositiveButton("Sí") { dialog, id ->
                task.done = !task.done
                taskDAO.updateTask(task)
                loadData()
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.dismiss()
            }
        builder.create().show()
    }






}