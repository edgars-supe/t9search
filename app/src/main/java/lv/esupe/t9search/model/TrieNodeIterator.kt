package lv.esupe.t9search.model

import java.util.*

class TrieNodeIterator(
    root: TrieNode
) : Iterator<TrieNode> {
    private val stack = Stack<TrieNode>()

    init {
        stack.push(root)
    }

    override fun hasNext(): Boolean = stack.size > 0

    override fun next(): TrieNode {
        if (stack.size == 0) {
            throw NoSuchElementException()
        }
        val node = stack.pop()
        node.children.forEach { child ->
            stack.push(child)
        }
        return node
    }
}