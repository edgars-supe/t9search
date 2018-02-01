package lv.esupe.t9search.model

import java.util.*

/**
 * An Iterator for [TrieNode].
 */
class TrieNodeIterator(
    root: TrieNode
) : Iterator<TrieNode> {
private val queue = LinkedList<TrieNode>()

    init {
        queue.add(root)
    }

    override fun hasNext(): Boolean = queue.isNotEmpty()

    override fun next(): TrieNode {
        if (queue.isEmpty()) {
            throw NoSuchElementException()
        }
        val node = queue.removeFirst()
        queue.addAll(node.children)
        return node
    }
}