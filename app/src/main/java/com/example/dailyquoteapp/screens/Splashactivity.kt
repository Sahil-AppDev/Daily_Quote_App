package com.example.dailyquoteapp.screens

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.dailyquoteapp.databinding.ActivitySplashactivityBinding
import com.example.dailyquoteapp.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class Splashactivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashactivityBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({

                startActivity(Intent(this, MainActivity::class.java))


            finish()

        }, 3000)
    }
}
