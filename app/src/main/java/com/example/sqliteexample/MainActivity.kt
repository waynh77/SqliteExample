package com.example.sqliteexample

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var editTextId: EditText
    private lateinit var editTextName: EditText
    private lateinit var editTextAge: EditText
    private lateinit var buttonAdd: Button
    private lateinit var buttonView: Button
    private lateinit var buttonUpdate: Button
    private lateinit var buttonDelete: Button
    private lateinit var textViewUsers: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)

        editTextId = findViewById(R.id.editTextId)
        editTextName = findViewById(R.id.editTextName)
        editTextAge = findViewById(R.id.editTextAge)
        buttonAdd = findViewById(R.id.buttonAdd)
        buttonView = findViewById(R.id.buttonView)
        buttonUpdate = findViewById(R.id.buttonUpdate)
        buttonDelete = findViewById(R.id.buttonDelete)
        textViewUsers = findViewById(R.id.textViewUsers)

        // 1. ADD USER
        buttonAdd.setOnClickListener {
            val name = editTextName.text.toString()
            val age = editTextAge.text.toString().toIntOrNull() ?: 0

            if (name.isNotEmpty() && age > 0) {
                databaseHelper.addUser(name, age)
                Toast.makeText(this, "User Added", Toast.LENGTH_SHORT).show()
                clearInputs()
                displayUsers()
            } else {
                Toast.makeText(this, "Please enter valid name and age", Toast.LENGTH_SHORT).show()
            }
        }

        // 2. VIEW USERS
        buttonView.setOnClickListener {
            displayUsers()
        }

        // 3. UPDATE USER
        buttonUpdate.setOnClickListener {
            val id = editTextId.text.toString().toIntOrNull()
            val name = editTextName.text.toString()
            val age = editTextAge.text.toString().toIntOrNull() ?: 0

            if (id != null && name.isNotEmpty() && age > 0) {
                val rowsUpdated = databaseHelper.updateUser(id, name, age)
                if (rowsUpdated > 0) {
                    Toast.makeText(this, "User Updated Successfully", Toast.LENGTH_SHORT).show()
                    clearInputs()
                    displayUsers()
                } else {
                    Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter ID, Name, and Age", Toast.LENGTH_SHORT).show()
            }
        }

        // 4. DELETE USER
        buttonDelete.setOnClickListener {
            val id = editTextId.text.toString().toIntOrNull()

            if (id != null) {
                val rowsDeleted = databaseHelper.deleteUser(id)
                if (rowsDeleted > 0) {
                    Toast.makeText(this, "User Deleted Successfully", Toast.LENGTH_SHORT).show()
                    clearInputs()
                    displayUsers()
                } else {
                    Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter a valid User ID", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayUsers() {
        val users = databaseHelper.getAllUsers()
        val displayText = StringBuilder("User List:\n\n")
        for (user in users) {
            displayText.append("ID: ${user.id}, Name: ${user.name}, Age: ${user.age}\n")
        }
        textViewUsers.text = displayText.toString()
    }

    private fun clearInputs() {
        editTextId.text.clear()
        editTextName.text.clear()
        editTextAge.text.clear()
    }
}