package com.example.dailyquoteapp.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyquoteapp.R
import com.example.dailyquoteapp.adapter.QuoteAdapter
import com.example.dailyquoteapp.viewModel.QuoteViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class Favorites : AppCompatActivity() {


        private lateinit var viewModel: QuoteViewModel
        private lateinit var adapter: QuoteAdapter

        @SuppressLint("MissingInflatedId")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_saved)

            viewModel = ViewModelProvider(this)[QuoteViewModel::class.java]
            adapter = QuoteAdapter()

            val recyclerView = findViewById<RecyclerView>(R.id.rvFavorites)
            val emptyText = findViewById<TextView>(R.id.tvEmpty)

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter

            viewModel.loadAllQuotesForFavorites()


            viewModel.favoriteQuotes.observe(this) { favorites ->
                if (favorites.isEmpty()) {
                    emptyText.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    emptyText.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    adapter.submitList(favorites)
                }
            }

            adapter.onFavoriteClick = { quote ->
                viewModel.toggleFavorite(quote)
            }
            val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

// Default selected
            bottomNav.selectedItemId = R.id.menu_home

            bottomNav.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_home -> {
                        // Already on Home
                        startActivity(Intent(this, MainActivity::class.java))
                        false
                    } R.id.menu_fav -> {
                    // Already on Home

                    true
                }

                    R.id.menu_profile -> {
                        startActivity(Intent(this, ProfileActivity::class.java))
                        false
                    }

                    else -> false
                }
            }


        }
    }



