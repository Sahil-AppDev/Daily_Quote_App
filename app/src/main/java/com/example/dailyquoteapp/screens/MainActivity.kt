package com.example.dailyquoteapp.screens

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SearchView

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.dailyquoteapp.R
import com.example.dailyquoteapp.adapter.QuoteAdapter
import com.example.dailyquoteapp.data.Quote
import com.example.dailyquoteapp.utils.ImageSaver
import com.example.dailyquoteapp.utils.NotificationUtils
import com.example.dailyquoteapp.viewModel.QuoteViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: QuoteViewModel
    private lateinit var adapter: QuoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val chipGroup = findViewById<ChipGroup>(R.id.chipGroupCategory)
        NotificationUtils.createChannel(this)


        adapter = QuoteAdapter()
        viewModel = ViewModelProvider(this)[QuoteViewModel::class.java]

        val recyclerView = findViewById<RecyclerView>(R.id.rvQuotes)
        val searchView = findViewById<SearchView>(R.id.searchView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.onShareClick = { quote ->
            showQuotePreview(quote)
        }


        val emptyState = findViewById<TextView>(R.id.tvEmptyState)
        viewModel.quoteOfTheDay.observe(this) { quote ->
            findViewById<TextView>(R.id.tvQodContent).text = quote.content
            findViewById<TextView>(R.id.tvQodAuthor).text = "— ${quote.author}"
        }

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
                    true
                } R.id.menu_fav -> {
                    // Already on Home
                startActivity(Intent(this, Favorites::class.java))
                    false
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
    override fun onResume() {
        super.onResume()
        viewModel.loadInitialQuotes()
    }

    private fun shareQuoteText(quote: Quote) {
        val shareText = """
        “${quote.content}”
        
        — ${quote.author}
        
        Shared via DailyQuoteApp ✨
    """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        startActivity(
            Intent.createChooser(intent, "Share quote via")
        )
    }
    private fun showQuotePreview(quote: Quote) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_quote_preview, null)
        dialog.setContentView(view)

        val container = view.findViewById<View>(R.id.cardContainer)
        val tvQuote = view.findViewById<TextView>(R.id.tvPreviewQuote)
        val tvAuthor = view.findViewById<TextView>(R.id.tvPreviewAuthor)
        val btnSave = view.findViewById<Button>(R.id.btnSaveImage)
        val btnShare = view.findViewById<Button>(R.id.btnShareImage)

        // bind data
        tvQuote.text = quote.content
        tvAuthor.text = "— ${quote.author}"

        // ⏳ wait until layout is ready
        container.post {
            val bitmap = captureView(container)

            btnSave.setOnClickListener {
                ImageSaver.saveToGallery(
                    this,
                    bitmap,
                    "quote_${System.currentTimeMillis()}"
                )
                Toast.makeText(this, "Saved to Gallery ✨", Toast.LENGTH_SHORT).show()
            }

            btnShare.setOnClickListener {
                shareBitmap(bitmap)
            }
        }

        dialog.show()
    }

    private fun captureView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(
            view.width,
            view.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
    private fun shareBitmap(bitmap: Bitmap) {
        val uri = ImageSaver.saveTempAndGetUri(this, bitmap)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(Intent.createChooser(intent, "Share image via"))
    }







}
