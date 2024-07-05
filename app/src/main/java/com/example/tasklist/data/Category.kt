package com.example.tasklist.data

import android.provider.BaseColumns

data class Category(
    var id: Int,
    var name: String
){  companion object {
    const val TABLE_CATEGORY = "Categories"
    const val COLUMN_NAME_CATEGORY = "name"

    val CREATE_TABLE = """
    CREATE TABLE $TABLE_CATEGORY (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        $COLUMN_NAME_CATEGORY TEXT )""".trimIndent()
}


}

