package com.example.tasklist.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.tasklist.utils.DatabaseHelper

class TaskDAO(val context: Context) {

    private var dbHelper: DatabaseHelper = DatabaseHelper(context)

    // Insertar task
    fun insertTask(task:Task): Boolean {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put(Task.COLUMN_NAME, task.name)
            put(Task.COLUMN_DONE, task.done)
            put(Task.COLUMN_DESCRIPTION ,task.description)
        }
        val result = db.insert(Task.TABLE_TASKS, null, contentValues)
        return result != -1L
    }

    // Obtener todos las tasks
    fun findAll(): Cursor {
        val db = dbHelper.readableDatabase
        return db.rawQuery("SELECT * FROM " + Task.TABLE_TASKS, null);
    }

    // Actualizar task
    fun updateTask(task:Task): Boolean {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put(Task.COLUMN_NAME, task.name)
            put(Task.COLUMN_DONE, task.done)
            put(Task.COLUMN_DESCRIPTION, task.description)
        }
        val result = db.update(
            Task.TABLE_TASKS,
            contentValues,
            "${Task.COLUMN_TASK_ID} = ?",
            arrayOf(task.id.toString()))
        return result > 0
    }

    // Eliminar task
    fun deleteTask(task:Task): Boolean {
        val db = dbHelper.writableDatabase
        val result = db.delete(Task.TABLE_TASKS, "${Task.COLUMN_TASK_ID} = ?", arrayOf(task.id.toString()))
        return result > 0
    }
}