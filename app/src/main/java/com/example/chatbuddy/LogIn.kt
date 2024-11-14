package com.example.chatbuddy

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LogIn : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogIn: Button
    private lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        mAuth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnLogIn = findViewById(R.id.btnLogIn)
        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        btnLogIn.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            // Validate input fields
            when {
                email.isEmpty() -> {
                    edtEmail.error = "Please enter your Email ID"
                    edtEmail.requestFocus()
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    edtEmail.error = "Please enter a valid Email ID"
                    edtEmail.requestFocus()
                }
                password.isEmpty() -> {
                    edtPassword.error = "Please enter your Password"
                    edtPassword.requestFocus()
                }
                password.length < 6 -> {
                    edtPassword.error = "Password must be at least 6 characters"
                    edtPassword.requestFocus()
                }
                else -> {
                    // Call the login function
                    showLoading(true)
                    login(email, password)
                }
            }
        }
    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            showLoading(false) // Hide loading after login attempt
            if (task.isSuccessful) {
                val intent = Intent(this@LogIn, MainActivity::class.java)
                finish()
                startActivity(intent)
            } else {
                Toast.makeText(this@LogIn, "Incorrect email or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to toggle loading state on login button
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            btnLogIn.text = "Logging in..."
            btnLogIn.isEnabled = false
        } else {
            btnLogIn.text = "Log In"
            btnLogIn.isEnabled = true
        }
    }
}
