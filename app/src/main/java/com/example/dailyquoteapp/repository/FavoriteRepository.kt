package com.example.dailyquoteapp.repository

import com.example.dailyquoteapp.data.Quote


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class FavoriteRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun saveFavorite(quote: Quote) {
        val userId = auth.currentUser?.uid ?: return

//        db.collection("favorites")
//            .document("${userId}_${quote.id}")
//            .set(
//                mapOf(
//                    "userId" to userId,
//                    "quoteId" to quote.id,
//                    "content" to quote.content,
//                    "author" to quote.author,
//                    "category" to quote.category,
//                    "createdAt" to FieldValue.serverTimestamp()
//                )
//            )
    }
}
