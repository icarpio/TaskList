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
            put(Task.COLUMN_DESCRIPTION ,task.description)
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
                put(Task.COLUMN_DESCRIPTION, task.description)
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


    fun findAll() : List<Task> {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(BaseColumns._ID, Task.COLUMN_NAME, Task.COLUMN_DONE,Task.COLUMN_DESCRIPTION)
        val cursor = db.query(
            Task.TABLE_TASKS,                        // The table to query
            projection,                             // The array of columns to return (pass null to get all)
            null,                            // The columns for the WHERE clause
            null,                         // The values for the WHERE clause
            null,                            // don't group the rows
            null,                             // don't filter by row groups
            null                             // The sort order
        )

        var tasks = mutableListOf<Task>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME))
            val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_DONE)) == 1
            val description = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_DESCRIPTION))
            val task = Task(id, name, done, description)
            tasks.add(task)
        }
        cursor.close()
        db.close()
        return tasks
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
                val description = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_DESCRIPTION))
                val task = Task(id, name, done, description)
                tasks.add(task)
                // Imprimir en el log
                Log.d("TaskRecord", "ID: $id, Name: $name, Done: $done, Description: $description")
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return tasks
    }
    fun find(id: Int) : Task? {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(BaseColumns._ID, Task.COLUMN_NAME, Task.COLUMN_DONE,Task.COLUMN_DESCRIPTION)

        val cursor = db.query(
            Task.TABLE_TASKS,                        // The table to query
            projection,                             // The array of columns to return (pass null to get all)
            "${BaseColumns._ID} = $id",      // The columns for the WHERE clause
            null,                         // The values for the WHERE clause
            null,                            // don't group the rows
            null,                             // don't filter by row groups
            null                             // The sort order
        )

        var task: Task? = null
        if (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME))
            val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_DONE)) == 1
            val description = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_DESCRIPTION))
            task = Task(id, name, done, description)
        }
        cursor.close()
        db.close()
        return task
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
            val description = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_DESCRIPTION))
            // Imprimir en el log
            Log.d("Task: ", "ID: $id, Name: $name, Done: $done, Description: $description")
        }
        db.close()
        return cursor
    }
}