package com.example.dailyquoteapp.api

import com.example.dailyquoteapp.data.Quote
import com.example.dailyquoteapp.data.QuoteDto
import com.google.gson.annotations.SerializedName
data class QuoteResponse(
    val page: Int,
    val totalPages: Int,
    val results: List<QuoteDto>
)



