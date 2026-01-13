package com.example.dailyquoteapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyquoteapp.data.Quote
import com.example.dailyquoteapp.databinding.ItemQuoteBinding

class QuoteAdapter : RecyclerView.Adapter<QuoteAdapter.VH>() {

    private val items = mutableListOf<Quote>()

    fun submit(list: List<Quote>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class VH(val binding: ItemQuoteBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(p: ViewGroup, v: Int): VH {
        return VH(
            ItemQuoteBinding.inflate(
                LayoutInflater.from(p.context), p, false
            )
        )
    }

    override fun onBindViewHolder(h: VH, i: Int) {
        val q = items[i]
        h.binding.tvQuote.text = q.content
        h.binding.tvAuthor.text = "— ${q.author}"
    }

    override fun getItemCount() = items.size
}
