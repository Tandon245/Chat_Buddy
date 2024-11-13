package com.example.chatbuddy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText

    private lateinit var mDbRef: DatabaseReference
    private lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        edtName = findViewById(R.id.edtName)
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnSignUp = findViewById(R.id.btnSignUp)

        mAuth = FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            val name = edtName.text.toString().trim()

            if (validateInput(name, email, password)) {
                signUp(name, email, password)
            }
        }
    }

    private fun validateInput(name: String, email: String, password: String): Boolean {
        return when {
            name.isEmpty() -> {
                edtName.error = "Please enter your name"
                edtName.requestFocus()
                false
            }
            email.isEmpty() -> {
                edtEmail.error = "Please enter your email"
                edtEmail.requestFocus()
                false
            }
            password.isEmpty() -> {
                edtPassword.error = "Please enter your password"
                edtPassword.requestFocus()
                false
            }
            password.length < 6 -> {
                edtPassword.error = "Password must be at least 6 characters"
                edtPassword.requestFocus()
                false
            }
            else -> true
        }
    }

    private fun signUp(name: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // User created successfully
                    addUserToDatabase(name, email, mAuth.currentUser?.uid!!)
                    val intent = Intent(this@SignUp, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Check if the exception is due to an existing user
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        showAlreadyRegisteredDialog()
                    } else {
                        Toast.makeText(this@SignUp, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun showAlreadyRegisteredDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("User Already Registered")
        builder.setMessage("This email is already registered. Please log in instead.")
        builder.setPositiveButton("Log In") { _, _ ->
            val intent = Intent(this@SignUp, LogIn::class.java)
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("Cancel", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(name, email, uid))
    }
}
