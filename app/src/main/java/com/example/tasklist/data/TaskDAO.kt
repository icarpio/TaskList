package com.example.tasklist.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import android.util.Log
import com.example.tasklist.utils.DatabaseHelper

class TaskDAO(val context: Context) {

    private var dbHelper: DatabaseHelper = DatabaseHelper(context)

    // Insertar task
    fun insertTask(task:Task) {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put(Task.COLUMN_NAME, task.name)
            put(Task.COLUMN_DONE, task.done)
            put(Task.COLUMN_DATEMAX ,task.dateMax)
        }
        val result = db.insert(Task.TABLE_TASKS, null, contentValues)
        task.id = result.toInt()
    }

    // Obtener todos las tasks

    // Actualizar task
    fun updateTask(task: Task): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val contentValues = ContentValues().apply {
                put(Task.COLUMN_NAME, task.name)
                put(Task.COLUMN_DONE, task.done)
                put(Task.COLUMN_DATEMAX, task.dateMax)
            }
            val result = db.update(
                Task.TABLE_TASKS,
                contentValues,
                "${BaseColumns._ID} = ?",
                arrayOf(task.id.toString())
            )
            Log.d("TaskUpdate", "Task updated with id: ${task.id}, Result: $result")
            result > 0  // Retorna true si al menos una fila fue actualizada
        } catch (e: Exception) {
            Log.e("TaskUpdate", "Error updating task", e)
            false
        } finally {
            db.close()
        }
    }

    // Eliminar task
    fun deleteTask(task:Task) {
        val db = dbHelper.writableDatabase
        val deletedRows = db.delete(Task.TABLE_TASKS, "${BaseColumns._ID} = ${task.id}", null)
        Log.i("DELETE", "Deleted: $deletedRows")
    }


    fun getAllRecords(): List<Task> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM " + Task.TABLE_TASKS, null)
        var tasks = mutableListOf<Task>()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME))
                val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_DONE)) == 1
                val dateMax = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_DATEMAX))
                val task = Task(id, name, done, dateMax)
                tasks.add(task)
                // Imprimir en el log
                //Log.d("TaskRecord", "ID: $id, Name: $name, Done: $done, Description: $description")
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return tasks
    }

    fun getRecordById(id: Int): Cursor {
        val db = dbHelper.readableDatabase

        // Construir la consulta SQL
        val query = """
        SELECT * FROM ${Task.TABLE_TASKS} WHERE ${BaseColumns._ID} = ? """.trimIndent()

        // Ejecutar la consulta
        val cursor = db.rawQuery(query, arrayOf(id.toString()))

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME))
            val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_DONE)) == 1
            val dateMax = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_DATEMAX))
            // Imprimir en el log
            Log.d("Task: ", "ID: $id, Name: $name, Done: $done, Description: $dateMax")
        }
        db.close()
        return cursor
    }
}