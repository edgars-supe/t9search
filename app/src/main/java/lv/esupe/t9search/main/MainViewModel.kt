package lv.esupe.t9search.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import lv.esupe.t9search.model.Trie
import lv.esupe.t9search.model.ValueKeyResolver
import java.io.BufferedReader
import java.io.InputStream


class MainViewModel : ViewModel() {
    val mainState = MutableLiveData<MainState>()
    private lateinit var trie: Trie

    fun init(dictionaryInputStream: InputStream, resolver: ValueKeyResolver) {
        val bufferedReader = BufferedReader(dictionaryInputStream.reader())
        val words = ArrayList<String>()
        bufferedReader.forEachLine { word ->
            words.add(word)
        }

        trie = Trie(resolver)
        trie.loadDictionary(words)
    }

    fun onSearchClicked(term: String) {
        val words = trie.lookup(term)
        val subList = words.subList(0, Math.min(50, words.lastIndex))
        mainState.value = MainState(term, subList)
    }
}