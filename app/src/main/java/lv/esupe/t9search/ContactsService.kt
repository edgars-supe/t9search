package lv.esupe.t9search

import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.support.v4.app.JobIntentService


class ContactsService : JobIntentService() {
    companion object {
        const val JOB_ID = 76486
        const val INTENT_LOAD_CONTACTS = "lv.esupe.t9search.INTENT_LOAD_CONTACTS"
        const val RESULT_CONTACTS_LOADED = "lv.esupe.t9search.RESULT_CONTACTS_LOADED"

        fun createLoadContactsIntent(context: Context): Intent {
            val intent = Intent(context, ContactsService::class.java)
            intent.action = INTENT_LOAD_CONTACTS
            return intent
        }
    }
    private val uri = ContactsContract.Contacts.CONTENT_URI
    private val projection = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
    private val sort = "${ContactsContract.Contacts.DISPLAY_NAME} ASC"

    override fun onHandleWork(intent: Intent) {
        when (intent.action) {
            INTENT_LOAD_CONTACTS -> loadContacts()
        }
    }

    private fun loadContacts() {
        val cursor = contentResolver.query(uri, projection, null, null, sort)
        val names = (0 until cursor.count).map {
            cursor.moveToNext()
            cursor.getString(0)
        }
        cursor.close()

        (applicationContext as App).contacts.loadContacts(names)
        onContactsLoaded()
    }


    private fun onContactsLoaded() {
        val intent = Intent(RESULT_CONTACTS_LOADED)
        sendBroadcast(intent)
    }
}