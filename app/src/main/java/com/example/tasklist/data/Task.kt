package com.example.tasklist.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns


data class Task(
    var id: Int,
    var name: String,
    var done: Boolean,
    var dateMax: String
) {

    companion object {

        //Tabla categorias


        const val TABLE_CATEGORY = "Categories"
        const val COLUMN_NAME_CATEGORY = "name"

        // Tabla de tareas
        const val TABLE_TASKS = "Tasks"
        const val COLUMN_NAME = "name"
        const val COLUMN_DONE = "done"
        const val COLUMN_DATEMAX = "datemax"
        const val COLUMN_CATEGORY_ID = "category_id"

        val CREATE_TABLE = """
    CREATE TABLE $TABLE_TASKS (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        $COLUMN_NAME TEXT,
        $COLUMN_DONE INTEGER,
        $COLUMN_DATEMAX TEXT,
        $COLUMN_CATEGORY_ID INTEGER,
        FOREIGN KEY ($COLUMN_CATEGORY_ID) REFERENCES $TABLE_CATEGORY(${BaseColumns._ID}) ON DELETE CASCADE
    )
""".trimIndent()

        val CREATE_CATEGORY = """
    CREATE TABLE $TABLE_CATEGORY (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        $COLUMN_NAME_CATEGORY TEXT
    )
""".trimIndent()

    }
}
