package lv.esupe.t9search.model


object T9Trie {
    private val root = TrieNode('0')
    private val validKeys = '2'..'9'

    fun loadDictionary(words: Iterable<String>) {
        words.forEach { word ->
            val keys = resolve(word)
            var node = root
            keys.forEach { key ->
                node = node.getNodeForKey(key)
            }
            node.values.add(word)
        }
    }

    fun lookup(digits: String): List<String> {
        var node = root
        digits.forEach { key ->
            if (key in validKeys) {
                node = node.getNodeForKey(key)
            }
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
     * Removes invalid keys (not in '2'..'9') from the term and returns the sanitized term
     */
    fun sanitize(term: String): String {
        val chars = term.filter { char ->
            char in validKeys
        }
        val charArray = chars.toCharArray()
        return String(charArray)
    }

    private fun resolve(word: String): List<Char> {
        return word.mapNotNull { char ->
            when (char) {
                in 'a'..'c' -> '2'
                in 'd'..'f' -> '3'
                in 'g'..'i' -> '4'
                in 'j'..'l' -> '5'
                in 'm'..'o' -> '6'
                in 'p'..'s' -> '7'
                in 't'..'v' -> '8'
                in 'w'..'z' -> '9'
                else -> null
            }
        }
    }
}