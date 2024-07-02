package com.example.tasklist.adapters

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklist.R
import com.example.tasklist.data.Task
import com.example.tasklist.databinding.TaskItemBinding

class TaskAdapter(
    private var dataSet: List<Task> = emptyList(),
    private val onItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.render(dataSet[position])
        holder.itemView.setOnClickListener {
            onItemClickListener(position)
        }
    }

    override fun getItemCount(): Int = dataSet.size

    fun updateData(dataSet: List<Task>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }
}


class TaskViewHolder(private val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun render(task: Task) {
        binding.nameTextView.text = task.name

    }
}