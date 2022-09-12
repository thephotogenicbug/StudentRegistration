package com.naveen.studentregistration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.naveen.studentregistration.databinding.ActivityMainBinding
import com.naveen.studentregistration.db.Student
import com.naveen.studentregistration.db.StudentDatabase

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

//  private lateinit var nameEditText: EditText
//  private lateinit var emailEditText: EditText
//  private lateinit var saveButton: Button
//  private lateinit var clearButton: Button

  private lateinit var viewModel: StudentViewModel
//  private lateinit var studentRecyclerView: RecyclerView
  private lateinit var adapter: StudentRecyclerViewAdaptor

  private var isListItemClicked = false

  // Delete var
  private lateinit var selectedStudent:Student



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        nameEditText = findViewById(R.id.etName)
//        emailEditText = findViewById(R.id.etEmail)
//        saveButton = findViewById(R.id.btnSave)
//        clearButton = findViewById(R.id.btnClear)
//        studentRecyclerView = findViewById(R.id.rvStudent)

        val dao = StudentDatabase.getInstance(application).studentDao()
        val factory = StudentViewModelFactory(dao)
        viewModel = ViewModelProvider(this, factory).get(StudentViewModel::class.java)

        binding.apply{
        btnSave.setOnClickListener {
            if(isListItemClicked){
                updateStudentData()
                clearInput()
            }else {
                saveStudentData()
                clearInput()
            }
        }


        btnClear.setOnClickListener {
            if(isListItemClicked){
                deleteStudentData()
                clearInput()
            }else {
                clearInput()
            }
        }
        }
        initRecyclerView()
    }

    private fun saveStudentData(){
//        val name = nameEditText.text.toString()
//        val email = emailEditText.text.toString()
//        val student = Student(0,name,email)
//        viewModel.insertStudent(student)
       binding.apply{
        viewModel.insertStudent(
            Student(
                0,
                etName.text.toString(),
                etEmail.text.toString()

            )
        )
       }
    }

    private fun updateStudentData(){
        binding.apply {
        viewModel.updateStudent(
            Student(
                selectedStudent.id,
                etName.text.toString(),
                etEmail.text.toString()
            )
        )
        btnSave.text = "Save"
        btnClear.text = "Clear"
        isListItemClicked = false
        }
    }

    private fun deleteStudentData(){
        binding.apply{
        viewModel.deleteStudent(
            Student(
                selectedStudent.id,
                etName.text.toString(),
                etEmail.text.toString()
            )
        )
        btnSave.text = "Save"
        btnClear.text = "Clear"
        isListItemClicked = false
        }
    }

    private fun clearInput(){
        binding.apply {
            etName.setText("")
            etEmail.setText("")
        }
    }

    private fun initRecyclerView(){
        binding.rvStudent.layoutManager = LinearLayoutManager(this)
        adapter = StudentRecyclerViewAdaptor{
            selectedItem:Student -> listItemClicked(selectedItem)
        }
        binding.rvStudent.adapter = adapter

        displayStudentsList()
    }

    private fun displayStudentsList(){
        viewModel.students.observe(this,{
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(student: Student){
        binding.apply {
         selectedStudent = student
        btnSave.text = "Update"
        btnClear.text = "Delete"
        isListItemClicked = true
        etName.setText(selectedStudent.name)
        etEmail.setText(selectedStudent.email)
        }
    }
}