package com.mdev.todo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBHandler internal constructor(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_STUDENT_TABLE = ("CREATE TABLE " +
                TABLE_STUDENTS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME
                + " TEXT " + ")")
        db.execSQL(CREATE_STUDENT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,
                           newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENTS")
        onCreate(db)
    }

    fun loadHandler(): String {
        var result = ""
        val query = "Select*FROM $TABLE_STUDENTS"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val result_0 = cursor.getInt(0)
            val result_1 = cursor.getString(1)
            result += result_0.toString() + " " + result_1 +
                    System.getProperty("line.separator")
        }
        cursor.close()
        db.close()
        if (result == "") result = "No Record Found"
        return result
    }

    fun addHandler(student: Student): Long {
        val id: Long
        val values = ContentValues()
        values.put(COLUMN_ID, student.studentID)
        values.put(COLUMN_NAME, student.studentName)
        val db = this.writableDatabase
        id = db.insert(TABLE_STUDENTS, null, values)
        db.close()
        return id
    }

    fun updateHandler(student: Student): Boolean {
        val db = this.writableDatabase
        val args = ContentValues()
        args.put(COLUMN_ID, student.studentID)
        args.put(COLUMN_NAME, student.studentName)
        return db.update(TABLE_STUDENTS, args, "$COLUMN_ID=${student.studentID}", null) > 0
    }

    fun deleteHandler(ID: Int): Boolean {
        var result = false
        val query = "Select*FROM $TABLE_STUDENTS WHERE $COLUMN_ID = '$ID'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val id= cursor.getString(0).toInt()
            db.delete(TABLE_STUDENTS, "$COLUMN_ID=?", arrayOf(
                java.lang.String.valueOf(id)
            ))
            cursor.close()
            result = true
        }
        db.close()
        return result
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "studentDB.db"
        private const val TABLE_STUDENTS = "students"
        private const val COLUMN_ID = "StudentID"
        private const val COLUMN_NAME = "StudentName"
    }
}