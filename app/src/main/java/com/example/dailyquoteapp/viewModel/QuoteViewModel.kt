package com.example.dailyquoteapp.viewModel

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyquoteapp.App
import com.example.dailyquoteapp.R
import com.example.dailyquoteapp.data.Quote
import com.example.dailyquoteapp.repository.QuoteRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QuoteViewModel : ViewModel() {

    private val repository = QuoteRepository()

    private val allQuotes = mutableListOf<Quote>()
    private val pagedQuotes = mutableListOf<Quote>()
    private val _quoteOfTheDay = MutableLiveData<Quote>()
    val quoteOfTheDay: LiveData<Quote> = _quoteOfTheDay

    private val _quotes = MutableLiveData<List<Quote>>()
    val quotes: LiveData<List<Quote>> = _quotes

    private var currentIndex = 0
    private val pageSize = 10
    private var isLoading = false

    // 🔍 filters
    private var activeQuery = ""
    private var activeCategory = "All"
    private val _favoriteQuotes = MutableLiveData<List<Quote>>()
    val favoriteQuotes: LiveData<List<Quote>> = _favoriteQuotes

    /* ---------------- INITIAL LOAD ---------------- */

    fun loadInitialQuotes() {
        viewModelScope.launch {
            // 1️⃣ Reset state
            allQuotes.clear()
            pagedQuotes.clear()
            currentIndex = 0

            // 2️⃣ Fetch quotes once
            allQuotes.addAll(repository.fetchQuotes())

            // 3️⃣ Restore favorites state
            loadFavorites()

            // 4️⃣ Pick / restore Quote of the Day
            loadQuoteOfTheDay()

            // 5️⃣ Load first page for Home feed
            loadNextPage()
        }
    }


    /* ---------------- PAGINATION ---------------- */

    fun loadNextPage() {
        if (isLoading) return
        if (isFiltering()) return
        if (currentIndex >= allQuotes.size) return

        isLoading = true

        viewModelScope.launch {
            delay(600)

            val nextIndex =
                (currentIndex + pageSize).coerceAtMost(allQuotes.size)

            pagedQuotes.addAll(
                allQuotes.subList(currentIndex, nextIndex)
            )

            currentIndex = nextIndex
            _quotes.value = pagedQuotes.toList()
            isLoading = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadQuoteOfTheDay() {
        val prefs = App.context.getSharedPreferences("quote_of_day", Context.MODE_PRIVATE)

        val today = java.time.LocalDate.now().toString()
        val savedDate = prefs.getString("date", null)
        val savedQuoteId = prefs.getString("quote_id", null)

        if (savedDate == today && savedQuoteId != null) {
            // reuse saved quote
            allQuotes.find { it.id == savedQuoteId }?.let {
                _quoteOfTheDay.value = it
                return
            }
        }

        // pick new quote
        if (allQuotes.isNotEmpty()) {
            val newQuote = allQuotes.random()
            prefs.edit()
                .putString("date", today)
                .putString("quote_id", newQuote.id)
                .apply()

            _quoteOfTheDay.value = newQuote
        }
    }


    /* ---------------- SEARCH ---------------- */

    fun search(query: String) {
        activeQuery = query
        applyFilters()
    }

    /* ---------------- CATEGORY ---------------- */

    fun setCategory(category: String) {
        activeCategory = category
        applyFilters()
    }

    /* ---------------- FILTER LOGIC ---------------- */

    private fun applyFilters() {
        if (!isFiltering()) {
            _quotes.value = pagedQuotes.toList()
            return
        }

        val filtered = allQuotes.filter {
            matchesQuery(it) && matchesCategory(it)
        }

        _quotes.value = filtered
    }

    private fun isFiltering(): Boolean {
        return activeQuery.isNotBlank() || activeCategory != "All"
    }

    private fun matchesQuery(q: Quote): Boolean {
        return q.content.contains(activeQuery, ignoreCase = true) ||
                q.author.contains(activeQuery, ignoreCase = true)
    }

    private fun matchesCategory(q: Quote): Boolean {
        if (activeCategory == "All") return true

        val text = q.content.lowercase()

        return when (activeCategory) {
            "Motivation" -> listOf("success", "goal", "dream").any { it in text }
            "Humor" -> listOf("funny", "laugh", "joke").any { it in text }
            "Life" -> listOf("life", "living").any { it in text }
            "Love" -> listOf("love", "heart").any { it in text }
            "Wisdom" -> listOf("wisdom", "truth", "knowledge").any { it in text }
            else -> true
        }
    }
    fun getFavoriteQuotes(): List<Quote> {
        return allQuotes.filter { it.isFavorite }
    }

    fun toggleFavorite(quote: Quote) {
        quote.isFavorite = !quote.isFavorite
        saveFavorites()

        // ✅ update favorites stream
        _favoriteQuotes.value = allQuotes.filter { it.isFavorite }

        // ✅ refresh home feed so heart updates
        _quotes.value = pagedQuotes.toList()
    }


    fun loadAllQuotesForFavorites() {
        viewModelScope.launch {
            allQuotes.clear()
            allQuotes.addAll(repository.fetchQuotes())
            loadFavorites()

            _favoriteQuotes.value = allQuotes.filter { it.isFavorite }
        }
    }


    private fun saveFavorites() {
        val prefs = App.context
            .getSharedPreferences("favorites", Context.MODE_PRIVATE)

        val ids = allQuotes
            .filter { it.isFavorite }
            .map { it.id }
            .toSet()

        prefs.edit()
            .putStringSet("favorite_ids", ids)
            .apply()
    }

    private fun loadFavorites() {
        val prefs = App.context
            .getSharedPreferences("favorites", Context.MODE_PRIVATE)

        val ids = prefs.getStringSet("favorite_ids", emptySet()) ?: emptySet()

        allQuotes.forEach {
            it.isFavorite = ids.contains(it.id)
        }
    }



}


