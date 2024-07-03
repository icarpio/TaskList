package com.example.tasklist.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns


data class Task(
    var id: Int,
    val name: String,
    var done: Boolean,
    var dateMax:String) {

    companion object {

        // Tabla de personas
        const val TABLE_TASKS = "Tasks"
        const val COLUMN_NAME = "name"
        const val COLUMN_DONE = "done"
        const val COLUMN_DATEMAX = "datemax"

        val CREATE_TABLE = """
            CREATE TABLE $TABLE_TASKS (
                ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_DONE INTEGER,
                $COLUMN_DATEMAX TEXT
                
            )
            """.trimIndent()
    }

}
