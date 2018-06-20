package lv.esupe.t9search.view

data class ContactView(val name: String, val highlightStart: Int, val length: Int) {
    val highlightEnd = highlightStart + length
}