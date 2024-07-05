package com.example.tasklist.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import android.util.Log
import com.example.tasklist.utils.DatabaseHelper

class CategoryDAO (context: Context) {

    private var dbHelper: DatabaseHelper = DatabaseHelper(context)
    fun insertCategory(category: Category) {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put(Category.COLUMN_NAME_CATEGORY, category.name)
        }
        val result = db.insert(Category.TABLE_CATEGORY, null, contentValues)
        category.id = result.toInt()
    }
    fun deleteCategory(category:Category) {
        val db = dbHelper.writableDatabase
        val deletedRows = db.delete(Category.TABLE_CATEGORY, "${BaseColumns._ID} = ${category.id}", null)
        Log.i("DELETE", "Deleted: $deletedRows")
    }

    fun getAllCategories(): List<Category> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM " + Category.TABLE_CATEGORY, null)
        var categories = mutableListOf<Category>()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(Category.COLUMN_NAME_CATEGORY))
                val category = Category(id, name)
                categories.add(category)
                Log.d("Category", "ID: $id, Name: $name")
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return categories
    }

    fun getCategoryById(id: Int): Category? {
        val db = dbHelper.readableDatabase

        // Construir la consulta SQL
        val query = """
        SELECT * FROM ${Category.TABLE_CATEGORY} WHERE ${BaseColumns._ID} = ?
    """.trimIndent()

        // Ejecutar la consulta
        val cursor = db.rawQuery(query, arrayOf(id.toString()))
        var category: Category? = null

        if (cursor.moveToFirst()) {
            val categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Category.COLUMN_NAME_CATEGORY))
            category = Category(categoryId, name)
        }
        cursor.close()
        return category
    }


}