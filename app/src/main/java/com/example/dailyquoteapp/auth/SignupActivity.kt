package com.example.dailyquoteapp.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dailyquoteapp.R
import com.google.firebase.auth.FirebaseAuth


class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)
        val btnSignup = findViewById<Button>(R.id.btnSignup)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        btnSignup.setOnClickListener {
            val e = email.text.toString().trim()
            val p = password.text.toString().trim()

            if (e.isEmpty() || p.length < 6) {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 🔄 SHOW LOADER
            progressBar.visibility = View.VISIBLE
            btnSignup.isEnabled = false

            auth.createUserWithEmailAndPassword(e, p)
                .addOnSuccessListener {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
                .addOnCompleteListener {
                    // 🔄 HIDE LOADER
                    progressBar.visibility = View.GONE
                    btnSignup.isEnabled = true
                }
        }
    }
}
