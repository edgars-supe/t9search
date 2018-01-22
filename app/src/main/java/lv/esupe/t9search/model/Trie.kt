package lv.esupe.t9search.model


class Trie(
    private val keyResolver: ValueKeyResolver
) {
    private val root = TrieNode('0')

    fun loadDictionary(words: Iterable<String>) {
        words.forEach { word ->
            val keys = keyResolver.resolve(word)
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
            if (key in '2'..'9') {
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
}