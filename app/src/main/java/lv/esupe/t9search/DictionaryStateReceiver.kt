package lv.esupe.t9search

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class DictionaryStateReceiver(
    var onLoaded: (() -> Unit)?
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == DictionaryService.RESULT_DICTIONARY_LOADED) {
            onLoaded?.invoke()
        }
    }
}