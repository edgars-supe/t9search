package lv.esupe.t9search

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.support.annotation.RawRes
import java.io.BufferedReader
import java.io.InputStream


class DictionaryService : IntentService(TAG) {
    companion object {
        const val TAG = "DictionaryService"
        const val INTENT_LOAD_DICTIONARY = "lv.esupe.t9search.INTENT_LOAD_DICTIONARY"
        const val RESULT_DICTIONARY_LOADED = "lv.esupe.t9search.RESULT_DICTIONARY_LOADED"
        private const val EXTRA_RAW_RES_ID = "EXTRA_RAW_RES_ID"

        fun createLoadDictionaryIntent(context: Context, @RawRes rawResId: Int): Intent {
            val intent = Intent(context, DictionaryService::class.java)
            intent.action = INTENT_LOAD_DICTIONARY
            intent.putExtra(EXTRA_RAW_RES_ID, rawResId)
            return intent
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            INTENT_LOAD_DICTIONARY -> handleLoadDictionaryIntent(intent)
        }
    }

    private fun handleLoadDictionaryIntent(intent: Intent) {
        val rawResId = intent.getIntExtra(EXTRA_RAW_RES_ID, -1)
        if (rawResId == -1) {
            return
        }

        val inputStream = resources.openRawResource(rawResId)
        val words = loadWords(inputStream)
        inputStream.close()
        (applicationContext as App).dictionary.loadDictionary(words)

        onDictionaryLoaded()
    }

    private fun loadWords(inputStream: InputStream): List<String> {
        val bufferedReader = BufferedReader(inputStream.reader())
        val words = ArrayList<String>()
        bufferedReader.forEachLine { word ->
            words.add(word)
        }
        bufferedReader.close()

        return words
    }

    private fun onDictionaryLoaded() {
        val intent = Intent(RESULT_DICTIONARY_LOADED)
        sendBroadcast(intent)
    }
}