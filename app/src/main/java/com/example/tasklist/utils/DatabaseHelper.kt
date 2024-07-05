package com.example.tasklist.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tasklist.data.Category
import com.example.tasklist.data.Task

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "task-list.db"
        private const val DATABASE_VERSION = 3
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Task.CREATE_TABLE)
        db.execSQL(Category.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${Task.TABLE_TASKS}")
        db.execSQL("DROP TABLE IF EXISTS ${Category.TABLE_CATEGORY}")
        onCreate(db)
    }

}


