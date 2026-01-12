package com.example.dailyquoteapp

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyquoteapp.adapter.FavoritesAdapter
import com.example.dailyquoteapp.data.FavoriteManager
import com.example.dailyquoteapp.databinding.ActivitySavedBinding

class Saved : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
private lateinit var binding: ActivitySavedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivitySavedBinding.inflate(layoutInflater)
     setContentView(binding.root)
        val list = FavoriteManager.getAll(this)

        binding.rvFavorites.layoutManager = LinearLayoutManager(this)
        binding.rvFavorites.adapter = FavoritesAdapter(list)

        binding.tvCount.text = "${list.size} Quotes Saved"


    }


}