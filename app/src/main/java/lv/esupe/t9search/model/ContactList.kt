package lv.esupe.t9search.model

import java.text.Normalizer


class ContactList : Contacts {
    override val contacts = ArrayList<Contact>()
    private var isLoaded = false

    override fun loadContacts(names: Iterable<String>) {
        val mapped = names.map { name ->
            Contact(name, transform(name))
        }
        contacts.addAll(mapped)
        isLoaded = true
    }

    override fun areContactsLoaded(): Boolean = isLoaded

    override fun filter(term: String): List<Contact> {
        return contacts.filter { contact ->
            contact.name.contains(term, ignoreCase = true)
        }
    }

    override fun filterT9(term: String): List<Contact> {
        return contacts.filter { contact ->
            contact.t9.contains(term)
        }
    }

    /**
     * Transforms a word into its T9 representation
     *
     * @return T9 representation of `word`
     */
    private fun transform(word: String): String {
        return word.mapNotNull { char ->
            transform(char)
        }.joinToString("")
    }

    /**
     * Converts `char` to its T9 representation
     *
     * @return T9 representation of `char` or `null` if none found
     */
    private fun transform(char: Char?): Char? {
        return when {
            char == null -> '-' // if no character can be found, insert '-', so highlight indices work properly
            char in 'a'..'c' || char in 'A'..'C' -> '2'
            char in 'd'..'f' || char in 'D'..'F' -> '3'
            char in 'g'..'i' || char in 'G'..'I' -> '4'
            char in 'j'..'l' || char in 'J'..'L' -> '5'
            char in 'm'..'o' || char in 'M'..'O' -> '6'
            char in 'p'..'s' || char in 'P'..'S' -> '7'
            char in 't'..'v' || char in 'T'..'V' -> '8'
            char in 'w'..'z' || char in 'W'..'Z' -> '9'
            char.isWhitespace() -> '0'
            char.isDigit() -> char
            char.isLetter() -> transform(normalizeChar(char)) // accents and diacritics
            else -> '-'
        }
    }

    /**
     * Normalizes the given `char` - replaces an accented letter with its non-accented counterpart
     *
     * @return Non-diacritic or accented version of `char` or `null`, if such a version can't be found
     */
    private fun normalizeChar(char: Char): Char? {
        val asString = char.toString()
        return Normalizer.normalize(asString, Normalizer.Form.NFD)
                .replace(Regex("\\p{InCombiningDiacriticalMarks}+"),"")
                .firstOrNull()
    }
}