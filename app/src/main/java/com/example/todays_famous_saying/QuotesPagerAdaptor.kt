package com.example.todays_famous_saying

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuotesPagerAdaptor(
    private val quotes: List<Quote>,
    private val isNameOn: Boolean
): RecyclerView.Adapter<QuotesPagerAdaptor.QuoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder
    = QuoteViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quote, parent, false)
    )

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val actualPosition = position % quotes.size
        holder.bind(quotes[actualPosition], isNameOn)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val quoteTextView: TextView = itemView.findViewById(R.id.quoteTextView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

        @SuppressLint("SetTextI18n")
        fun bind(quote: Quote, isNameOn: Boolean) {
            quoteTextView.text = "\"${quote.quote}\""
            if (isNameOn) {
                nameTextView.text = "- ${quote.name}  "
                nameTextView.visibility = View.VISIBLE
            } else {
                nameTextView.visibility = View.GONE
            }
        }
    }

}