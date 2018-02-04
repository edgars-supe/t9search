package lv.esupe.t9search.main

import android.text.Editable
import android.text.TextWatcher


class SearchTextWatcher(
    val onChanged: (term: String) -> Unit
) : TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        s?.let {
            onChanged(s.toString())
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }
}