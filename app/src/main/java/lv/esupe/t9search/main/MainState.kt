package lv.esupe.t9search.main


/**
 * A class that denotes the state of the Main view.
 */
data class MainState(
    val term: String,
    val words: List<String>
)