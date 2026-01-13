package com.example.dailyquoteapp.repository

import com.example.dailyquoteapp.api.ApiClient
import com.example.dailyquoteapp.data.Quote
import com.example.dailyquoteapp.data.QuoteCategory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class QuoteRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getQuotes(category: String? = null): List<Quote> {
        val query = if (category == null) {
            db.collection("quotes")
        } else {
            db.collection("quotes").whereEqualTo("category", category)
        }

        val snapshot = query.get().await()

        return snapshot.documents.map { doc ->
            Quote(
                id = doc.id,
                content = doc.getString("content") ?: "",
                author = doc.getString("author") ?: "",
                category = doc.getString("category") ?: ""
            )
        }
    }
}

