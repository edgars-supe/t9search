package lv.esupe.t9search.model


class T9Trie : Dictionary {
    private val root = TrieNode('0')
    private val validKeys = '2'..'9'
    private var isDictionaryLoaded = false

    /**
     * Loads the given list of `words` into a Trie structure.
     *
     * @param words List of words to store
     */
    override fun loadDictionary(words: Iterable<String>) {
        words.forEach { word ->
            val keys = transform(word)
            var node = root
            keys.forEach { key ->
                node = node.getNodeForKey(key)
            }
            node.values.add(word)
        }
        isDictionaryLoaded = true
    }

    /**
     * Performs a search for words matching possible letter combinations using the given `digits`
     * as if on a numeric keypad.
     *
     * @param digits Digits of a numeric keypad to match words against
     */
    override fun lookup(digits: String): List<String> {
        var node = root
        val term = sanitize(digits)
        term.forEach { key ->
            node = node.getNodeForKey(key)
        }
        val values = ArrayList<String>()
        if (node != root) {
            node.forEach { n ->
                values.addAll(n.values)
            }
        }
        return values
    }

    /**
     * Returns information on whether a dictionary has been loaded.
     *
     * @return `true` if a dictionary has been loaded, `false` otherwise
     */
    override fun isDictionaryLoaded(): Boolean = isDictionaryLoaded

    /**
     * Removes invalid characters (not in `'2'..'9'`) from `term`.
     *
     * @param term The term to sanitize.
     * @return `term` with invalid keys removed
     */
    private fun sanitize(term: String): String {
        return term.filter { char ->
            char in validKeys
        }
    }

    /**
     * Transforms `word` into a list of digits, matching each character to its corresponding
     * number value on a numeric keypad.
     *
     * @param word The word to transform
     * @return List of digits that represent `word` on a numeric keypad
     */
    private fun transform(word: String): List<Char> {
        return word.mapNotNull { char ->
            when (char) {
                in 'a'..'c', in 'A'..'C' -> '2'
                in 'd'..'f', in 'D'..'F' -> '3'
                in 'g'..'i', in 'G'..'I' -> '4'
                in 'j'..'l', in 'J'..'L' -> '5'
                in 'm'..'o', in 'M'..'O' -> '6'
                in 'p'..'s', in 'P'..'S' -> '7'
                in 't'..'v', in 'T'..'V' -> '8'
                in 'w'..'z', in 'W'..'Z' -> '9'
                else -> null
            }
        }
    }
}