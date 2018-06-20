# T9 Search
This app filters the user's contact list based on plain-text or T9 search. It highlights the matched string as well.
The dictionary is read from a [word list](https://github.com/edgars-supe/t9search/blob/master/app/src/main/res/raw/wordlist) and stored in a [Trie](https://en.wikipedia.org/wiki/Trie). This allows us to easily find all words that match and are prefixed by a given key.  
The results are output using `RecyclerView`.  
The architecture of choice is MVVM, as made possible by [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/index.html). This makes it easier to ensure separation of concerns - `MainActivity` is responsible for the UI, `MainViewModel` is responsible for the business logic, and `T9Trie` is responsible for the dictionary.  

## User-facing requirements
> Instead of dictionary of words, search over the device’s contact names
> On app launch all contact names should be displayed, and filtered as you type

Read Contacts permission is required for this, and is asked for when the user opens the app. If the permission is not given, a Toast appears.  
Contacts are read using a cursor. While reading the contacts, a T9 representation of their display names is also generated.

> Accept both digits and letters, in case ONLY digits are entered do a T9-search, otherwise, do a plain string search over the names

Whether or not it's a T9 search is checked by Regex matching the term with `\d+`, i.e., it contains only digits. When creating the T9 representation of contacts' names, diacritics and accents are stripped, meaning that "3" matches both "e" and "ē" (and other variations).

> Highlight matched part of the name

Done using `SpannableString`.

> In case no contacts match the filter, display a message in the middle of the screen “No contacts to display”

Done.
