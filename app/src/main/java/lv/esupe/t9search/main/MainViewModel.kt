package lv.esupe.t9search.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import lv.esupe.t9search.model.Dictionary


class MainViewModel : ViewModel() {
    val mainState = MutableLiveData<MainState>()
    var maxResults = 50
    private lateinit var dictionary: Dictionary
    private var isInitialized = false

    /**
     * Initializes the ViewModel by setting the `dictionary` to use.
     *
     * @param dictionary Dictionary to be used for searching
     */
    fun init(dictionary: Dictionary) {
        if (isInitialized) {
            return
        }
        this.dictionary = dictionary

        if (dictionary.isDictionaryLoaded()) {
            mainState.postValue(MainState.Idle)
        } else {
            mainState.postValue(MainState.Loading)
        }

        isInitialized = true
    }

    fun onDictionaryLoaded() {
        mainState.postValue(MainState.Idle)
    }

    /**
     * To be called when the user clicks the search button.
     */
    fun onSearchClicked(term: String) {
        if(!isInitialized) {
            throw IllegalStateException("init() must be called before a search can be made")
        }
        val words = dictionary.lookup(term)
        val subList = words.subList(0, Math.min(maxResults - 1, words.size))
        mainState.value = MainState.Loaded(subList)
    }
}