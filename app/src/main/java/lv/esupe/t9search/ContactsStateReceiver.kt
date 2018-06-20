package lv.esupe.t9search

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class ContactsStateReceiver(
    var onLoaded: (() -> Unit)?
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ContactsService.RESULT_CONTACTS_LOADED) {
            onLoaded?.invoke()
        }
    }
}