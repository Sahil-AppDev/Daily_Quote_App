package com.example.dailyquoteapp.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface QuoteApi {

    @GET("quotes")
    suspend fun getQuotes(): List<ZenQuoteDto>
}





