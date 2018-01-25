package lv.esupe.t9search.model

import java.util.*

/**
 * An Iterator for [TrieNode].
 */
class TrieNodeIterator(
    root: TrieNode
) : Iterator<TrieNode> {
private val queue = ArrayList<TrieNode>()

    init {
        queue.add(root)
    }

    override fun hasNext(): Boolean = queue.size > 0

    override fun next(): TrieNode {
        if (queue.size == 0) {
            throw NoSuchElementException()
        }
        val node = queue.removeAt(0)
        queue.addAll(node.children)
        return node
    }
}