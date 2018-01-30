package lv.esupe.t9search.main


/**
 * A class that denotes the state of the Main view.
 */
sealed class MainState {
    object Loading : MainState()
    object Idle : MainState()
    data class Loaded(val words: List<String>) : MainState()
}