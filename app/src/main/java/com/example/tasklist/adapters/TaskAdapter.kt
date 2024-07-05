package com.example.tasklist.adapters

import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.tasklist.R
import com.example.tasklist.data.CategoryDAO
import com.example.tasklist.data.Task
import com.example.tasklist.databinding.TaskItemBinding
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class TaskAdapter(
    private var dataSet: List<Task> = emptyList(),
    private val onItemClickListener: (Int) -> Unit,
    private val onItemDeleteClickListener: (Int) -> Unit,
    private val onItemCheckedClickListener: (Int) -> Unit,
) : RecyclerView.Adapter<TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }
    override fun getItemCount(): Int = dataSet.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.render(dataSet[position])

        /*
        holder.itemView.setOnClickListener {
            onItemClickListener(position)
        }*/
        holder.binding.editButton.setOnClickListener {
            // Puedes tener una acción diferente para el botón de edición si es necesario
            onItemClickListener(position)
        }
        holder.binding.deleteButton.setOnClickListener {
            onItemDeleteClickListener(position)
        }

        holder.binding.doneCheckBox.setOnCheckedChangeListener { buttonView, _ ->
            if(buttonView.isPressed){
                onItemCheckedClickListener(position)
            }
        }
    }

    fun updateData(dataSet: List<Task>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }
}


class TaskViewHolder(val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun render(task: Task) {
        binding.nameTextView.text = task.name
        binding.doneCheckBox.isChecked = task.done
        val colorGreen = binding.root.context.getColor(R.color.success)
        val appContext = itemView.context.applicationContext

        val catDAO = CategoryDAO(appContext)


        val category = catDAO.getCategoryById(task.categoryId)
        val cat = category?.name

        binding.categoryTextView.text = cat
        binding.doneCheckBox.isChecked = task.done



        // Verificar si faltan menos de 24 horas
        if (!task.dateMax.isNullOrEmpty()) {
            try {
                // Verificar si faltan menos de 24 horas
                val dateMax = LocalDateTime.parse(task.dateMax, DateTimeFormatter.ISO_DATE_TIME)


                val now = LocalDateTime.now()

                // Definir el formateador de fecha
                val formateador = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                // Formatear la fecha como un String
                val fechaFormateada = dateMax.format(formateador)

                binding.dateMaxTextView.text = fechaFormateada
                if (Duration.between(now, dateMax).toHours() in 13..24) {
                    binding.nameTextView.setTextColor(colorGreen)
                } else if (Duration.between(now, dateMax).toHours() < 12) {
                    binding.nameTextView.setTextColor(Color.RED)
                    binding.iconImageView.visibility = View.VISIBLE
                }else {
                    binding.nameTextView.setTextColor(Color.BLACK)
                }
            } catch (e: DateTimeParseException) {
                // Manejo del error de parseo
                binding.nameTextView.setTextColor(Color.BLACK)
                Log.e("TaskViewHolder", "Error parsing dateMax: ${task.dateMax}", e)
            }
        } else {
            // Manejo del caso cuando dateMax es nulo o vacío
            binding.nameTextView.setTextColor(Color.BLACK)
        }

    }
}