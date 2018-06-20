package lv.esupe.t9search.view

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView


class ResultViewHolder(private val view: TextView) : RecyclerView.ViewHolder(view) {
    fun bind(contact: ContactView) {
        val spannable = SpannableString(contact.name)
        if (contact.length > 0) {
            spannable.setSpan(
                    ForegroundColorSpan(Color.BLUE),
                    contact.highlightStart,
                    contact.highlightEnd,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        view.text = spannable
    }
}