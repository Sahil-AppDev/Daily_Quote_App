package com.example.dailyquoteapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.dailyquoteapp.api.ApiClient
import com.example.dailyquoteapp.data.FavoriteManager
import com.example.dailyquoteapp.data.Quote
import com.example.dailyquoteapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch



class MainActivity : ComponentActivity() {
    private lateinit var binding:ActivityMainBinding
    private var currentQuote: Quote? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            try {
                Log.d("API_CALL", "Starting quote fetch")

                val response = ApiClient.api.getRandomQuote()[0]

                binding.tvQuote.text = "“${response.quote}”"
                binding.tvAuthor.text = "— ${response.author}"

                currentQuote = Quote(
                    id = response.quote.hashCode().toString(),
                    content = response.quote,
                    author = response.author
                )


                binding.btnFavorite.isEnabled = true
                binding.btnShare.isEnabled = true

            } catch (e: Exception) {


                binding.tvQuote.text = "Failed to load quote"
                Toast.makeText(
                    this@MainActivity,
                    "Network error: ${e.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        binding.btnFavorite.setOnClickListener {
            currentQuote?.let {
                FavoriteManager.save(this, it)
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Saved::class.java)
                startActivity(intent)
            }
        }

        binding.btnShare.setOnClickListener {
            currentQuote?.let {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "“${it.content}” — ${it.author}"
                    )
                }
                startActivity(Intent.createChooser(intent, "Share Quote"))
            }
        }






    }
}


