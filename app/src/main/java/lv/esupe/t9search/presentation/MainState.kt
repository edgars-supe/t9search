package lv.esupe.t9search.presentation

import lv.esupe.t9search.view.ContactView


/**
 * A class that denotes the state of the Main view.
 */
sealed class MainState {
    object Loading : MainState()
    object Empty : MainState()
    data class Loaded(val contacts: List<ContactView>) : MainState()
}