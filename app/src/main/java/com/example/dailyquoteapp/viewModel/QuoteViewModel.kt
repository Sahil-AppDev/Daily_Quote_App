package com.example.dailyquoteapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyquoteapp.data.Quote
import com.example.dailyquoteapp.repository.QuoteRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QuoteViewModel : ViewModel() {

    private val repository = QuoteRepository()

    private val allQuotes = mutableListOf<Quote>()
    private val pagedQuotes = mutableListOf<Quote?>()

    private val _quotes = MutableLiveData<List<Quote?>>()
    val quotes: LiveData<List<Quote?>> = _quotes

    private var currentIndex = 0
    private val pageSize = 10

    private var isLoading = false
    private var activeQuery = ""
    private var activeCategory = "All"

    /* ---------------- INIT ---------------- */

    fun loadInitialQuotes() {
        viewModelScope.launch {
            allQuotes.clear()
            pagedQuotes.clear()
            currentIndex = 0

            allQuotes.addAll(repository.fetchQuotes())
            loadNextPage()
        }
    }

    /* ---------------- PAGINATION ---------------- */

    fun loadNextPage() {
        if (isLoading) return
        if (isFiltering()) return
        if (currentIndex >= allQuotes.size) return

        isLoading = true

        // show loader
        pagedQuotes.add(null)
        _quotes.value = pagedQuotes.toList()

        viewModelScope.launch {
            delay(700)

            // remove loader safely
            if (pagedQuotes.isNotEmpty()) {
                pagedQuotes.removeAt(pagedQuotes.lastIndex)
            }

            val nextIndex = (currentIndex + pageSize).coerceAtMost(allQuotes.size)
            pagedQuotes.addAll(allQuotes.subList(currentIndex, nextIndex))
            currentIndex = nextIndex

            _quotes.value = pagedQuotes.toList()
            isLoading = false
        }
    }

    /* ---------------- FILTERING ---------------- */

    fun setCategory(category: String) {
        activeCategory = category
        applyFilters()
    }

    fun search(query: String) {
        activeQuery = query
        applyFilters()
    }

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
        return q.content.contains(activeQuery, true)
                || q.author.contains(activeQuery, true)
    }

    private fun matchesCategory(q: Quote): Boolean {
        if (activeCategory == "All") return true
        val text = q.content.lowercase()

        return when (activeCategory) {
            "Motivation" -> listOf("success", "goal", "dream", "hard").any { it in text }
            "Humor" -> listOf("funny", "laugh", "joke").any { it in text }
            "Life" -> listOf("life", "living").any { it in text }
            "Love" -> listOf("love", "heart").any { it in text }
            "Wisdom" -> listOf("wisdom", "truth", "knowledge").any { it in text }
            else -> true
        }
    }
}
