package com.example.dailyquoteapp.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object FavoriteManager {

    private const val PREF_NAME = "quotes"
    private const val KEY_FAVORITES = "favorites"

    fun save(context: Context, quote: Quote) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val type = object : TypeToken<MutableList<Quote>>() {}.type
        val list = gson.fromJson<MutableList<Quote>>(
            prefs.getString(KEY_FAVORITES, "[]"),
            type
        )

//        if (list.none { it.id == quote.id }) {
//            list.add(quote)
//            prefs.edit().putString(KEY_FAVORITES, gson.toJson(list)).apply()
//        }
    }

    fun getAll(context: Context): List<Quote> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val type = object : TypeToken<List<Quote>>() {}.type
        return gson.fromJson(prefs.getString(KEY_FAVORITES, "[]"), type)
    }
}
