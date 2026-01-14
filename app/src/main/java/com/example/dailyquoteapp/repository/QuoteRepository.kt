package com.example.dailyquoteapp.repository

import com.example.dailyquoteapp.api.ApiClient
import com.example.dailyquoteapp.data.Quote
class QuoteRepository {

    suspend fun fetchQuotes(): List<Quote> {
        val response = ApiClient.api.getQuotes()

        return response.map {
            Quote(
                id = it.q.hashCode().toString(),
                content = it.q,
                author = it.a,
                category = "General"
            )
        }
    }
}

