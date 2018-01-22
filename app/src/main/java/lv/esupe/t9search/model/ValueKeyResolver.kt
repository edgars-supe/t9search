package lv.esupe.t9search.model



interface ValueKeyResolver {
    fun resolve(input: String): List<Char>
}