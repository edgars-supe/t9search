package lv.esupe.t9search

import android.app.Application
import lv.esupe.t9search.model.T9Trie
import java.io.BufferedReader


class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val inputStream = resources.openRawResource(R.raw.wordlist)
        val bufferedReader = BufferedReader(inputStream.reader())
        val words = ArrayList<String>()
        bufferedReader.forEachLine { word ->
            words.add(word)
        }
        bufferedReader.close()
        inputStream.close()

        T9Trie.loadDictionary(words)
    }
}