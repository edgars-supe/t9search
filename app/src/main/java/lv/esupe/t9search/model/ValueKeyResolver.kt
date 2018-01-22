package lv.esupe.t9search.model


/**
 * A class implements this interface to note that it can deconstruct a String into indices for a
 * Trie tree. 
 */
interface ValueKeyResolver {
    fun resolve(input: String): List<Char>
}