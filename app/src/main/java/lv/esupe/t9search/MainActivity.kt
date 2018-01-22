package lv.esupe.t9search

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import lv.esupe.t9search.model.T9Resolver
import lv.esupe.t9search.model.Trie
import java.io.BufferedReader

class MainActivity : AppCompatActivity() {
    private val t9Resolver = T9Resolver()
    private val trie = Trie(t9Resolver)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpViews()
        val inputStream = resources.openRawResource(R.raw.wordlist)
        val bufferedReader = BufferedReader(inputStream.reader())
        val words = ArrayList<String>()
        bufferedReader.forEachLine { word ->
            words.add(word)
        }
        trie.loadDictionary(words)
    }

    private fun setUpViews() {
        searchInput
        searchButton.setOnClickListener {
            val words = trie.lookup(searchInput.text.toString())
            val stringBuilder = StringBuilder()
            val sublist = words.subList(0, 49)
            sublist.forEach { word ->
                stringBuilder.appendln(word)
            }
            resultsView.text = stringBuilder.toString()
        }
    }
}
