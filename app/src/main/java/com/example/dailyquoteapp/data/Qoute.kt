package com.example.dailyquoteapp.data

data class Quote(
    val id: String,
    val content: String,
    val author: String,
    val category: String = "",
    var isFavorite: Boolean = false
)



