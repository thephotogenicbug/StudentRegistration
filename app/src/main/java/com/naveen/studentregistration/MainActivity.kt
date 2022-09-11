package com.naveen.studentregistration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.naveen.studentregistration.db.Student
import com.naveen.studentregistration.db.StudentDatabase

class MainActivity : AppCompatActivity() {
  private lateinit var nameEditText: EditText
  private lateinit var emailEditText: EditText
  private lateinit var saveButton: Button
  private lateinit var clearButton: Button

  private lateinit var viewModel: StudentViewModel
  private lateinit var studentRecyclerView: RecyclerView
  private lateinit var adapter: StudentRecyclerViewAdaptor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameEditText = findViewById(R.id.etName)
        emailEditText = findViewById(R.id.etEmail)
        saveButton = findViewById(R.id.btnSave)
        clearButton = findViewById(R.id.btnClear)
        studentRecyclerView = findViewById(R.id.rvStudent)

        val dao = StudentDatabase.getInstance(application).studentDao()
        val factory = StudentViewModelFactory(dao)
        viewModel = ViewModelProvider(this, factory).get(StudentViewModel::class.java)

        saveButton.setOnClickListener {
            saveStudentData()
            clearInput()
        }

        clearButton.setOnClickListener {
            clearInput()
        }
        initRecyclerView()
    }

    private fun saveStudentData(){
//        val name = nameEditText.text.toString()
//        val email = emailEditText.text.toString()
//        val student = Student(0,name,email)
//        viewModel.insertStudent(student)

        viewModel.insertStudent(
            Student(
                0,
                nameEditText.text.toString(),
                emailEditText.text.toString()

            )
        )
    }

    private fun clearInput(){
        nameEditText.setText("")
        emailEditText.setText("")
    }

    private fun initRecyclerView(){
        studentRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentRecyclerViewAdaptor()
        studentRecyclerView.adapter = adapter

        displayStudentsList()
    }

    private fun displayStudentsList(){
        viewModel.students.observe(this,{
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }
}