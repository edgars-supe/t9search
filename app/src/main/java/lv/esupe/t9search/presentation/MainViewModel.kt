package lv.esupe.t9search.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import lv.esupe.t9search.model.Contacts
import lv.esupe.t9search.view.ContactView


class MainViewModel : ViewModel() {
    val mainState: LiveData<MainState>
        get() = _mainState
    private val _mainState = MutableLiveData<MainState>()
    private lateinit var contacts: Contacts
    private val digitRegex = Regex("\\d+")
    private var isInitialized = false

    /**
     * Initializes the ViewModel by setting the `contacts` to use.
     *
     * @param contacts Contacts to be used for searching
     */
    fun init(contacts: Contacts) {
        if (isInitialized) {
            return
        }
        this.contacts = contacts

        if (this.contacts.areContactsLoaded()) {
            onContactsLoaded()
        } else {
            _mainState.postValue(MainState.Loading)
        }

        isInitialized = true
    }

    fun onContactsLoaded() {
        val contactViews = contacts.contacts.map { contact ->
            ContactView(contact.name, 0, 0)
        }

        _mainState.value = if (contactViews.isNotEmpty()) {
            MainState.Loaded(contactViews)
        } else {
            MainState.Empty
        }
    }

    /**
     * To be called when the user inputs a createContactView term.
     */
    fun onSearch(term: String) {
        if(!isInitialized) {
            throw IllegalStateException("init() must be called before a createContactView can be made")
        }

        if (term.matches(digitRegex)) {
            t9Search(term)
        } else {
            plainSearch(term)
        }
    }

    private fun t9Search(term: String) {
        val contactViews = contacts.filterT9(term).mapNotNull { contact ->
            createContactView(contact.name, contact.t9, term, false)
        }
        postResults(contactViews)
    }

    private fun plainSearch(term: String) {
        val contactViews = contacts.filter(term).mapNotNull { contact ->
            createContactView(contact.name, contact.name, term, true)
        }
        postResults(contactViews)
    }

    private fun createContactView(
            name: String,
            field: String,
            term: String,
            ignoreCase: Boolean
    ): ContactView? {
        val index = field.indexOf(term, ignoreCase = ignoreCase)
        return if (index >= 0) ContactView(name, index, term.length) else null
    }

    private fun postResults(results: List<ContactView>) {
        _mainState.value = if (results.isNotEmpty()) {
            MainState.Loaded(results)
        } else {
            MainState.Empty
        }
    }
}