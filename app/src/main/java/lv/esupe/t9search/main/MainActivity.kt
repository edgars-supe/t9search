package lv.esupe.t9search.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import lv.esupe.t9search.App
import lv.esupe.t9search.DictionaryService
import lv.esupe.t9search.DictionaryStateReceiver
import lv.esupe.t9search.R

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var dictionaryStateReceiver: DictionaryStateReceiver
    private val results = ArrayList<String>()
    private val adapter = ResultAdapter(results)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dictionary = (applicationContext as App).dictionary
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.init(dictionary)
        viewModel.mainState.observe(this, Observer { mainState ->
            onMainStateChanged(mainState)
        })
        dictionaryStateReceiver = DictionaryStateReceiver {
            viewModel.onDictionaryLoaded()
        }
        registerReceiver(
            dictionaryStateReceiver,
            IntentFilter(DictionaryService.RESULT_DICTIONARY_LOADED))

        setUpViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(dictionaryStateReceiver)
    }

    private fun setUpViews() {
        searchButton.setOnClickListener {
            viewModel.onSearchClicked(searchInput.text.toString())
        }

        resultsRecycler.adapter = adapter
        resultsRecycler.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun onMainStateChanged(mainState: MainState?) {
        when (mainState) {
            MainState.Idle -> onIdleState()
            MainState.Loading -> onLoadingState()
            is MainState.Loaded -> onLoadedState(mainState.words)
        }
    }

    private fun onIdleState() {
        resultsRecycler.visibility = View.VISIBLE
        loadingGroup.visibility = View.GONE
        searchButton.isEnabled = true
        searchInput.isEnabled = true
    }

    private fun onLoadingState() {
        resultsRecycler.visibility = View.GONE
        loadingGroup.visibility = View.VISIBLE
        searchButton.isEnabled = false
        searchInput.isEnabled = false
    }

    private fun onLoadedState(words: List<String>) {
        results.clear()
        results.addAll(words)
        adapter.notifyDataSetChanged()
    }
}
