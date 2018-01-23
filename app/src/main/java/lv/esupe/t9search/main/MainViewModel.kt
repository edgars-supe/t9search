package lv.esupe.t9search.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import lv.esupe.t9search.model.T9Trie


class MainViewModel : ViewModel() {
    val mainState = MutableLiveData<MainState>()
    var maxResults = 50

    /**
     * To be called when the user clicks the search button.
     */
    fun onSearchClicked(term: String) {
        val sanitized = T9Trie.sanitize(term)
        val words = T9Trie.lookup(sanitized)
        val subList = words.subList(0, Math.min(maxResults - 1, words.size))
        mainState.value = MainState(sanitized, subList)
    }
}