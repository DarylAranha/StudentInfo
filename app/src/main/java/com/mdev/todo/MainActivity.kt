package com.mdev.todo

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mdev.todo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var dbHandler: MyDBHandler? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        setContentView(R.layout.activity_main)

        binding.resultText.movementMethod = ScrollingMovementMethod()
        dbHandler = MyDBHandler(this)
    }

    fun loadStudents(view: View?) {
        binding.resultText.text = dbHandler!!.loadHandler()
        binding.studentId.setText("")
        binding.studentName.setText("")
    }

    fun addStudent(view: View?) {
        if (!binding.studentId.text.toString().isEmpty() && !binding.studentName.text.toString().isEmpty()) {
            val id = binding.studentId.text.toString().toInt()
            val name = binding.studentName.text.toString()
            val student = Student(id, name)
            val insertId = dbHandler!!.addHandler(student)
            if (insertId == -1L) {
                binding.resultText.text = "Record already exists"
            } else {
                binding.studentId.setText("")
                binding.studentName.setText("")
                binding.resultText.text = "Record added"
            }
        } else {
            binding.resultText.text = "Please fill correct id and name"
        }
    }

    fun updateStudent(view: View?) {
        if (!binding.studentId.text.toString().isEmpty() && !binding.studentName.text.toString().isEmpty()) {
            val id = binding.studentId.text.toString().toInt()
            val name = binding.studentName.text.toString()
            val student = Student(id, name)
            val result = dbHandler!!.updateHandler(student)
            if (result) {
                binding.studentId.setText("")
                binding.studentName.setText("")
                binding.resultText.text = "Record Updated"
            } else {
                binding.resultText.text = "No Record Found"
            }
        } else {
            binding.resultText.text = "Please fill correct id and name"
        }
    }

    fun deleteStudent(view: View?) {
        if (!binding.studentId.text.toString().isEmpty()) {
            val result = dbHandler!!.deleteHandler(
                binding.studentId.text.toString().toInt())
            if (result) {
                binding.studentId.setText("")
                binding.studentName.setText("")
                binding.resultText.text = "Record Deleted"
            } else {
                binding.resultText.text = "No Record Found"
            }
        } else {
            binding.resultText.text = "Please fill correct id"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHandler?.close()
    }
}