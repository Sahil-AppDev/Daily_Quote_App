package com.example.dailyquoteapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyquoteapp.R
import com.example.dailyquoteapp.data.Quote

class FavoritesAdapter(private val list: List<Quote>) :
    RecyclerView.Adapter<FavoritesAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val quote: TextView = view.findViewById(R.id.tvQuote)
        val author: TextView = view.findViewById(R.id.tvAuthor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite_quote, parent, false)
        return VH(view)
    }

    private val backgrounds = listOf(
        R.drawable.bg_quote_blue,
        R.drawable.bg_quote_purple,
        R.drawable.bg_quote_green,
        R.drawable.bg_quote_orange
    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        val quote = list[position]

        holder.quote.text = "\"${quote.content}\""
        holder.author.text = "- ${quote.author}"

        holder.itemView.background =
            ContextCompat.getDrawable(
                holder.itemView.context,
                backgrounds[position % backgrounds.size]
            )
    }


    override fun getItemCount() = list.size
}
