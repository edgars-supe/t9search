package lv.esupe.t9search.model

import java.util.*

/**
 * A node in a [T9Trie]. Contains a list of values whose keys match, starting from root, and the
 * node's children.
 */
class TrieNode(val key: Char) : Iterable<TrieNode> {
    val values = LinkedList<String>()
    val children = LinkedList<TrieNode>()

    override fun iterator(): Iterator<TrieNode> {
        return TrieNodeIterator(this)
    }

    /**
     * Returns a direct child node with the given key. If it doesn't exist, it is created and added
     * to the list of child nodes.
     *
     * @param key The specific key to look for in `children`
     * @return A child node with the given `key`
     */
    fun getNodeForKey(key: Char): TrieNode {
        var node = children.firstOrNull { child ->
            child.key == key
        }
        if (node == null) {
            node = TrieNode(key)
            children.add(node)
        }
        return node
    }
}