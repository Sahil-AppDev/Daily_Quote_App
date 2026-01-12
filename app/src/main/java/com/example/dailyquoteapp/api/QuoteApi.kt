package com.example.dailyquoteapp.api

import retrofit2.http.GET

interface QuoteApi {
    @GET("random")
    suspend fun getRandomQuote(): List<QuoteResponse>
}


