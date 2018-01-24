package lv.esupe.t9search.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import lv.esupe.t9search.model.Dictionary


class MainViewModel : ViewModel() {
    val mainState = MutableLiveData<MainState>()
    var maxResults = 50
    private lateinit var dictionary: Dictionary

    /**
     * Initializes the ViewModel by setting the `dictionary` to use.
     *
     * @param dictionary Dictionary to be used for searching
     */
    fun init(dictionary: Dictionary) {
        this.dictionary = dictionary
    }

    /**
     * To be called when the user clicks the search button.
     */
    fun onSearchClicked(term: String) {
        if(!this::dictionary.isInitialized) {
            throw IllegalStateException("init() must be called before a search can be made")
        }
        val words = dictionary.lookup(term)
        val subList = words.subList(0, Math.min(maxResults - 1, words.size))
        mainState.value = MainState(subList)
    }
}