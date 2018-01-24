package lv.esupe.t9search

import android.app.Application
import android.support.annotation.RawRes
import lv.esupe.t9search.model.Dictionary
import lv.esupe.t9search.model.T9Trie
import java.io.BufferedReader


class App : Application() {
    lateinit var dictionary: Dictionary // poor man's DI
        private set

    override fun onCreate() {
        super.onCreate()
        val words = loadWordList(R.raw.wordlist)

        dictionary = T9Trie()
        dictionary.loadDictionary(words)
    }

    private fun loadWordList(@RawRes listResId: Int): List<String> {
        val inputStream = resources.openRawResource(listResId)
        val bufferedReader = BufferedReader(inputStream.reader())
        val words = ArrayList<String>()
        bufferedReader.forEachLine { word ->
            words.add(word)
        }
        bufferedReader.close()
        inputStream.close()

        return words
    }
}