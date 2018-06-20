package lv.esupe.t9search.model


/**
 * Denotes that the implementing class can store contacts and filter them.
 */
interface Contacts {
    /**
     * A list of contacts on the phone.
     */
    val contacts: List<Contact>

    /**
     * Creates a list of contacts from the given names.
     */
    fun loadContacts(names: Iterable<String>)

    /**
     * Whether or not contacts have been loaded yet.
     */
    fun areContactsLoaded(): Boolean

    /**
     * Plain-text filtering over contacts' display name.
     *
     * @return List of [Contact] whose display names match `term`
     */
    fun filter(term: String): List<Contact>

    /**
     * T9 filtering over contacts's display name.
     *
     * @return List of [Contact] whose T9 representation of their display names match `term`
     */
    fun filterT9(term: String): List<Contact>
}