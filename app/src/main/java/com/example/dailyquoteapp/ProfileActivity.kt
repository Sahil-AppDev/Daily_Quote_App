package com.example.dailyquoteapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.example.dailyquoteapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var imgAvatar: ImageView
    private lateinit var tvEmail: TextView

    private val auth = FirebaseAuth.getInstance()

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imgAvatar.setImageURI(uri)
                saveAvatar(uri.toString())
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        imgAvatar = findViewById(R.id.imgAvatar)
        tvEmail = findViewById(R.id.tvEmail)

        // 🔐 Firebase email
        tvEmail.text = auth.currentUser?.email ?: "No email"

        // Load saved avatar
        loadAvatar()

        // Pick new image
        imgAvatar.setOnClickListener {
            imagePicker.launch("image/*")
        }
    }

    private fun saveAvatar(uri: String) {
        getSharedPreferences("profile", MODE_PRIVATE)
            .edit()
            .putString("avatar_uri", uri)
            .apply()
    }

    private fun loadAvatar() {
        val uri = getSharedPreferences("profile", MODE_PRIVATE)
            .getString("avatar_uri", null)

        if (uri != null) {
            imgAvatar.setImageURI(Uri.parse(uri))
        }
    }
}
