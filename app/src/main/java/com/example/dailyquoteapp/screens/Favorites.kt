package com.example.dailyquoteapp.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyquoteapp.R
import com.example.dailyquoteapp.adapter.QuoteAdapter
import com.example.dailyquoteapp.data.Quote
import com.example.dailyquoteapp.utils.ImageSaver
import com.example.dailyquoteapp.viewModel.QuoteViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog

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
            val tvCount = findViewById<TextView>(R.id.tvCount)

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter

            viewModel.loadAllQuotesForFavorites()
            adapter.onShareClick = { quote ->
                showQuotePreview(quote)
            }

            viewModel.favoriteQuotes.observe(this) { favorites ->
                if (favorites.isEmpty()) {
                    emptyText.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    tvCount.text = "0 quotes saved"
                } else {
                    emptyText.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    adapter.submitList(favorites)
                    tvCount.text = "${favorites.size} quotes saved"
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


                    else -> false
                }
            }


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



