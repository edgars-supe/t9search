package lv.esupe.t9search

import android.app.Application
import lv.esupe.t9search.model.Dictionary
import lv.esupe.t9search.model.T9Trie


class App : Application() {
    lateinit var dictionary: Dictionary // poor man's DI
        private set

    override fun onCreate() {
        super.onCreate()
        dictionary = T9Trie()

        val intent = DictionaryService.createLoadDictionaryIntent(this, R.raw.wordlist)
        startService(intent)
    }
}