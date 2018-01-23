package lv.esupe.t9search.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import kotlinx.android.synthetic.main.activity_main.*
import lv.esupe.t9search.R

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.mainState.observe(this, Observer { mainState ->
            onMainStateChanged(mainState)
        })
        setUpViews()
    }

    private fun setUpViews() {
        searchButton.setOnClickListener {
            viewModel.onSearchClicked(searchInput.text.toString())
        }
    }

    private fun onMainStateChanged(mainState: MainState?) {
        mainState?.let {
            val builder = SpannableStringBuilder()
            it.words.forEach { word ->
                val string = SpannableString(word)
                string.setSpan(
                    ForegroundColorSpan(Color.BLUE),
                    0,
                    it.term.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                builder.appendln(string)
            }
            resultsView.text = builder
        }
    }
}
