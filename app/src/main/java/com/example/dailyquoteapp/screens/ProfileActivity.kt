//package com.example.dailyquoteapp.screens
//
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import com.example.dailyquoteapp.R
//import com.example.dailyquoteapp.data.QuoteScheduler
//import com.google.android.material.bottomnavigation.BottomNavigationView
//import com.google.android.material.timepicker.MaterialTimePicker
//import com.google.android.material.timepicker.TimeFormat
//import com.google.firebase.auth.FirebaseAuth
//
//class ProfileActivity : AppCompatActivity() {
//
//    private lateinit var imgAvatar: ImageView
//    private lateinit var tvEmail: TextView
//
//    private val auth = FirebaseAuth.getInstance()
//
//    private val imagePicker =
//        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//            if (uri != null) {
//                imgAvatar.setImageURI(uri)
//                saveAvatar(uri.toString())
//            }
//        }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_profile)
//
//        imgAvatar = findViewById(R.id.imgAvatar)
//        tvEmail = findViewById(R.id.tvEmail)
//
//        // 🔐 Firebase email
//        tvEmail.text = auth.currentUser?.email ?: "No email"
//
//        // Load saved avatar
//        loadAvatar()
//
//        // Pick new image
//        imgAvatar.setOnClickListener {
//            imagePicker.launch("image/*")
//        }
//        val btnSetTime = findViewById<Button>(R.id.btnSetQuoteTime)
//
//        btnSetTime.setOnClickListener {
//
//            val picker = MaterialTimePicker.Builder()
//                .setTitleText("Select daily quote time")
//                .setHour(8)
//                .setMinute(0)
//                .setTimeFormat(TimeFormat.CLOCK_12H)
//                .build()
//
//            picker.show(supportFragmentManager, "time_picker")
//
//            picker.addOnPositiveButtonClickListener {
//                QuoteScheduler.scheduleDailyQuote(
//                    this,
//                    picker.hour,
//                    picker.minute
//                )
//
//                Toast.makeText(
//                    this,
//                    "Daily quote set for ${picker.hour}:${picker.minute}",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//
//
//        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
//
//        bottomNav.selectedItemId = R.id.menu_profile
//
//        bottomNav.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.menu_home -> {
//                    startActivity(Intent(this, MainActivity::class.java)) // go back to Home
//                    true
//                }
//                R.id.menu_fav -> {
//                    startActivity(Intent(this, Favorites::class.java))
//                    true
//                }
//                else -> false
//            }
//        }
//
//    }
//
//    private fun saveAvatar(uri: String) {
//        getSharedPreferences("profile", MODE_PRIVATE)
//            .edit()
//            .putString("avatar_uri", uri)
//            .apply()
//    }
//
//    private fun loadAvatar() {
//        val uri = getSharedPreferences("profile", MODE_PRIVATE)
//            .getString("avatar_uri", null)
//
//        if (uri != null) {
//            imgAvatar.setImageURI(Uri.parse(uri))
//        }
//    }
//}
