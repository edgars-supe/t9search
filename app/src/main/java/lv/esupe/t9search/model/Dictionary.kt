package lv.esupe.t9search.model


/**
 * Denotes that the implementing class can store a dictionary and perform searches in it.
 */
interface Dictionary {
    fun loadDictionary(words: Iterable<String>)
    fun lookup(digits: String): List<String>
    fun isDictionaryLoaded(): Boolean
}