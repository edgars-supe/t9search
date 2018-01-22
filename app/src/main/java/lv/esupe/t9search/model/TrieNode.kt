package lv.esupe.t9search.model

import java.util.*


class TrieNode(val key: Char) : Iterable<TrieNode> {
    val values = ArrayList<String>()
    val children = ArrayList<TrieNode>()

    override fun iterator(): Iterator<TrieNode> {
        return TrieNodeIterator(this)
    }

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