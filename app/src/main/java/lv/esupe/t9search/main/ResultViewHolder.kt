package lv.esupe.t9search.main

import android.support.v7.widget.RecyclerView
import android.widget.TextView


class ResultViewHolder(private val view: TextView) : RecyclerView.ViewHolder(view) {
    fun bind(word: String) {
        view.text = word
    }
}