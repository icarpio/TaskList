package com.example.tasklist.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tasklist.data.Task

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "task-list.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {

        val createTableCustomers = """
            CREATE TABLE ${Task.TABLE_TASKS} (
                ${Task.COLUMN_TASK_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${Task.COLUMN_NAME} TEXT,
                ${Task.COLUMN_DONE} INTEGER,
                ${Task.COLUMN_DESCRIPTION} TEXT
            )
            """.trimIndent()


        db.execSQL(createTableCustomers)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${Task.TABLE_TASKS}")
        onCreate(db)
    }

}


