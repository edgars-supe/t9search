package lv.esupe.t9search.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import lv.esupe.t9search.App
import lv.esupe.t9search.R

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
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

        setUpViews()
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
        mainState?.let {
            results.clear()
            results.addAll(it.words)
            adapter.notifyDataSetChanged()
        }
    }
}
