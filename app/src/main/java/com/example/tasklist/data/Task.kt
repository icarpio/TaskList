package com.example.tasklist.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


data class Task(var id: Int, val name: String, var done: Boolean, var description:String) {

    companion object {
        // Tabla de personas
        const val TABLE_TASKS = "Tasks"
        const val COLUMN_TASK_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_DONE = "done"
        const val COLUMN_DESCRIPTION = "description"
    }

}
