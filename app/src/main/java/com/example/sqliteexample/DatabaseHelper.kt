package com.example.sqliteexample

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "userDB.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USER = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_AGE = "age"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_USER_TABLE = ("CREATE TABLE $TABLE_USER ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_NAME TEXT, "
                + "$COLUMN_AGE INTEGER)")
        db?.execSQL(CREATE_USER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        onCreate(db)
    }
    // Menambahkan data pengguna
    fun addUser(name: String, age: Int) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NAME, name)
        contentValues.put(COLUMN_AGE, age)
        db.insert(TABLE_USER, null, contentValues)
        db.close()
    }

    // Mengambil semua data pengguna
    fun getAllUsers(): List<User> {
        val userList = ArrayList<User>()
        val selectQuery = "SELECT * FROM $TABLE_USER"

        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val user = User(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        age = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE))
                    )
                    userList.add(user)
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        db.close()
        return userList
    }

    fun updateUser(id: Int, newName: String, newAge: Int): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NAME, newName)
        contentValues.put(COLUMN_AGE, newAge)
        val success = db.update(TABLE_USER, contentValues, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return success
    }

    fun deleteUser(id: Int): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_USER, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return success
    }

}
