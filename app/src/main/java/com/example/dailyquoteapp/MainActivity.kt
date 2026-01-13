package com.example.dailyquoteapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyquoteapp.adapter.QuoteAdapter
import com.example.dailyquoteapp.data.Quote
import com.example.dailyquoteapp.data.QuoteCategory
import com.example.dailyquoteapp.databinding.ActivityMainBinding
import com.example.dailyquoteapp.repository.QuoteRepository
import com.example.dailyquoteapp.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val repo = QuoteRepository()
    private var allQuotes: List<Quote> = emptyList()
    private var selectedCategory: String? = null




    private val adapter = QuoteAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupCategories()
        loadQuotes()
        setupSearch()

        binding.swipeRefresh.setOnRefreshListener {
            loadQuotes()
        }
//        binding.logout.setOnClickListener {
//            FirebaseAuth.getInstance().signOut()
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//
//


    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun loadQuotes() {
        lifecycleScope.launch {
            try {
                binding.progressBar.visibility = View.VISIBLE
                allQuotes = repo.getQuotes()
                applyFilters()
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }
    private fun applyFilters() {
        val query = binding.searchView.query.toString()

        val filtered = allQuotes.filter { quote ->

            val matchesCategory =
                selectedCategory == null ||
                        quote.category == selectedCategory

            val matchesSearch =
                quote.content.contains(query, true) ||
                        quote.author.contains(query, true)

            matchesCategory && matchesSearch
        }

        adapter.submit(filtered)
    }






    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
    private fun setupCategories() {
        QuoteCategory.values().forEach { category ->

            val chip = layoutInflater.inflate(
                R.layout.item_category,
                binding.categoryContainer,
                false
            ) as TextView

            chip.text = category.displayName

            chip.setOnClickListener {
                selectedCategory = category.displayName
                applyFilters()
            }


            binding.categoryContainer.addView(chip)
        }
    }


    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                applyFilters()
                return true
            }
        })
    }


}
