package lv.esupe.t9search

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.support.v4.app.JobIntentService
import android.support.v4.content.ContextCompat
import lv.esupe.t9search.model.ContactList
import lv.esupe.t9search.model.Contacts


class App : Application() {
    lateinit var contacts: Contacts // poor man's DI
        private set

    override fun onCreate() {
        super.onCreate()
        contacts = ContactList()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            loadContacts()
        }
    }

    fun loadContacts() {
        val intent = ContactsService.createLoadContactsIntent(this)
        JobIntentService.enqueueWork(this, ContactsService::class.java, 0, intent)
    }
}