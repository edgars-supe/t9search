package lv.esupe.t9search.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import lv.esupe.t9search.model.Dictionary


class MainViewModel : ViewModel() {
    val mainState: LiveData<MainState>
        get() = _mainState
    private lateinit var dictionary: Dictionary
    private val _mainState = MutableLiveData<MainState>()
    private var isInitialized = false
    private var maxResults = 50

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
            _mainState.postValue(MainState.Idle)
        } else {
            _mainState.postValue(MainState.Loading)
        }

        isInitialized = true
    }

    fun onDictionaryLoaded() {
        _mainState.postValue(MainState.Idle)
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
        _mainState.value = MainState.Loaded(subList)
    }
}