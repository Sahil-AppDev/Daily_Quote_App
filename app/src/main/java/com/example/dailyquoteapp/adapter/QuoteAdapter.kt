package com.example.dailyquoteapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyquoteapp.R
import com.example.dailyquoteapp.data.Quote
import com.example.dailyquoteapp.databinding.ItemQuoteBinding
class QuoteAdapter : RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder>() {

    private val quotes = mutableListOf<Quote>()

    fun submitList(list: List<Quote>) {
        quotes.clear()
        quotes.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quote, parent, false)
        return QuoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = quotes[position]
        holder.bind(quote)

        // 🎨 Rotate background colors
        val backgrounds = listOf(
            R.drawable.bg_quote_blue,
            R.drawable.bg_quote_green,
            R.drawable.bg_quote_purple,
            R.drawable.bg_quote_orange
        )

        holder.itemView.background =
            ContextCompat.getDrawable(
                holder.itemView.context,
                backgrounds[position % backgrounds.size]
            )
    }

    override fun getItemCount(): Int = quotes.size

    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvQuote = itemView.findViewById<TextView>(R.id.tvQuote)
        private val tvAuthor = itemView.findViewById<TextView>(R.id.tvAuthor)

        fun bind(quote: Quote) {
            tvQuote.text = quote.content
            tvAuthor.text = quote.author
        }
    }
}
