package com.example.dailyquoteapp.api

import com.google.gson.annotations.SerializedName
data class QuoteResponse(
    @SerializedName("q") val quote: String,
    @SerializedName("a") val author: String
)


