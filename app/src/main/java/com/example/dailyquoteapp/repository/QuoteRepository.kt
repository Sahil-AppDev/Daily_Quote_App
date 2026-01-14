package com.example.dailyquoteapp.repository

import android.content.Context
import com.example.dailyquoteapp.App
import com.example.dailyquoteapp.api.ApiClient
import com.example.dailyquoteapp.data.Quote
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class QuoteRepository {

    suspend fun fetchQuotes(): List<Quote> {
        val prefs = App.context.getSharedPreferences("quotes_cache", Context.MODE_PRIVATE)
        val cached = prefs.getString("quotes_json", null)

        if (cached != null) {
            return Gson().fromJson(
                cached,
                object : TypeToken<List<Quote>>() {}.type
            )
        }

        val response = ApiClient.api.getQuotes()

        val quotes = response.map {
            Quote(
                id = "${it.q}_${it.a}".hashCode().toString(),
                content = it.q,
                author = it.a,
                category = "General"
            )
        }

        prefs.edit()
            .putString("quotes_json", Gson().toJson(quotes))
            .apply()

        return quotes
    }
}



