package com.example.chatbuddy

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
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
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val name = edtName.text.toString()

            if (validateInput(name, email, password)) {
                showLoading(true)
                signUp(name, email, password)
            }
        }
    }

    private fun validateInput(name: String, email: String, password: String): Boolean {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            return false
        }

        val passwordPattern = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*[@#\$%^&+=])(?=.*\\d).{8,}$")
        if (!password.matches(passwordPattern)) {
            Toast.makeText(
                this,
                "Password must contain at least 1 uppercase, 1 lowercase, 1 special character, 1 number, and be at least 8 characters long",
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }

    private fun signUp(name: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                showLoading(false) // Hide loader after signup attempt
                if (task.isSuccessful) {
                    addUserToDatabase(name, email, mAuth.currentUser?.uid!!)
                    Toast.makeText(this, "Signup successful! Please log in.", Toast.LENGTH_SHORT).show()
                    // Redirect to Chat activity
                    val intent = Intent(this@SignUp, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "User already exists or signup failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(name, email, uid))
    }

    // Function to toggle loading state on sign up button
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            btnSignUp.text = "Signing up..."
            btnSignUp.isEnabled = false
            btnSignUp.setBackgroundColor(resources.getColor(R.color.grey))
        } else {
            btnSignUp.text = "Sign Up"
            btnSignUp.isEnabled = true
        }
    }
}
