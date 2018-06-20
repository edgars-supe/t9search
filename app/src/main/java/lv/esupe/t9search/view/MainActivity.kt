package lv.esupe.t9search.view

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import lv.esupe.t9search.App
import lv.esupe.t9search.ContactsService
import lv.esupe.t9search.ContactsStateReceiver
import lv.esupe.t9search.R
import lv.esupe.t9search.presentation.MainState
import lv.esupe.t9search.presentation.MainViewModel

class MainActivity : AppCompatActivity() {
    private val permissionRequestCode = 12345
    private lateinit var viewModel: MainViewModel
    private lateinit var contactsStateReceiver: ContactsStateReceiver
    private val results = ArrayList<ContactView>()
    private val adapter = ResultAdapter(results)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val contacts = (applicationContext as App).contacts
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.init(contacts)
        viewModel.mainState.observe(this, Observer { mainState ->
            onMainStateChanged(mainState)
        })
        contactsStateReceiver = ContactsStateReceiver {
            viewModel.onContactsLoaded()
        }
        registerReceiver(
            contactsStateReceiver,
            IntentFilter(ContactsService.RESULT_CONTACTS_LOADED))

        setUpViews()
        checkReadContactsPermission()
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                (application as App).loadContacts()
            } else {
                Toast.makeText(this, R.string.activity_main_permission, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(contactsStateReceiver)
    }

    private fun setUpViews() {
        searchInput.addTextChangedListener(SearchTextWatcher { term ->
            viewModel.onSearch(term)
        })

        resultsRecycler.adapter = adapter
        resultsRecycler.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun checkReadContactsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    permissionRequestCode)
        }
    }

    private fun onMainStateChanged(mainState: MainState?) {
        when (mainState) {
            MainState.Loading -> onLoadingState()
            MainState.Empty -> onEmptyState()
            is MainState.Loaded -> onLoadedState(mainState.contacts)
        }
    }

    private fun onLoadingState() {
        resultsRecycler.visibility = View.GONE
        loadingProgressBar.visibility = View.VISIBLE
        infoText.visibility = View.VISIBLE
        infoText.setText(R.string.activity_main_loading)
        searchInput.isEnabled = false
    }

    private fun onEmptyState() {
        resultsRecycler.visibility = View.GONE
        loadingProgressBar.visibility = View.GONE
        infoText.visibility = View.VISIBLE
        infoText.setText(R.string.activity_main_empty)
        searchInput.isEnabled = true
    }

    private fun onLoadedState(words: List<ContactView>) {
        resultsRecycler.visibility = View.VISIBLE
        loadingProgressBar.visibility = View.GONE
        infoText.visibility = View.GONE
        searchInput.isEnabled = true
        results.clear()
        results.addAll(words)
        adapter.notifyDataSetChanged()
    }
}
