package com.example.dailyquoteapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.dailyquoteapp.adapter.QuoteAdapter
import com.example.dailyquoteapp.api.ApiClient
import com.example.dailyquoteapp.data.Quote
import com.example.dailyquoteapp.data.QuoteCategory
import com.example.dailyquoteapp.databinding.ActivityMainBinding
import com.example.dailyquoteapp.repository.QuoteRepository
import com.example.dailyquoteapp.ui.auth.LoginActivity
import com.example.dailyquoteapp.viewModel.QuoteViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: QuoteViewModel
    private lateinit var adapter: QuoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val chipGroup = findViewById<ChipGroup>(R.id.chipGroupCategory)


        adapter = QuoteAdapter()
        viewModel = ViewModelProvider(this)[QuoteViewModel::class.java]

        val recyclerView = findViewById<RecyclerView>(R.id.rvQuotes)
        val searchView = findViewById<SearchView>(R.id.searchView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val emptyState = findViewById<TextView>(R.id.tvEmptyState)

        viewModel.quotes.observe(this) { list ->
            val safeList = list.filterNotNull()

            adapter.submitList(safeList)

            if (safeList.isEmpty()) {
                emptyState.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                emptyState.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

// Default selected
        bottomNav.selectedItemId = R.id.menu_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
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
        val swipeRefresh = findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)

        swipeRefresh.setOnRefreshListener {
            viewModel.loadInitialQuotes()
            swipeRefresh.isRefreshing = false
        }

        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val chip = group.findViewById<Chip>(checkedIds[0])
                viewModel.setCategory(chip.text.toString())
            }
        }


        // Initial load
        viewModel.loadInitialQuotes()

        // Fake pagination trigger
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                if (!rv.canScrollVertically(1)) {
                    viewModel.loadNextPage()
                }
            }
        })

        // 🔍 SEARCH LISTENER
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText.orEmpty())
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.search(query.orEmpty())
                return true
            }
        })


    }
}
