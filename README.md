# T9 Search
This app takes user input in the form of digits and outputs the words that it finds in the dictionary, which can be made from combinations of letters those digits represent on a numeric keypad. Additionally, it outputs all words that are prefixed by the exact matches. Since `0` is a space and `1` is for vocemail, these keys are ignored in the query.  
The dictionary is read from a [word list](https://github.com/edgars-supe/t9search/blob/master/app/src/main/res/raw/wordlist) and stored in a [Trie](https://en.wikipedia.org/wiki/Trie). This allows us to easily find all words that match and are prefixed by a given key.  
The results are output using `RecyclerView`.  
The architecture of choice is MVVM, as made possible by [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/index.html). This makes it easier to ensure separation of concerns - `MainActivity` is responsible for the UI, `MainViewModel` is responsible for the business logic, and `T9Trie` is responsible for the dictionary.  

## User-facing requirements
> The app should:    
> - Have a minimal UI with a search box to enter a query and a list to show matching results.  

I originally opted to output the results in a single `TextView`, separating the words with a line break, because I thought that inflating 30-odd `TextView`s in a `RecyclerView` would be inefficient and cause stuttering. But that wouldn't be a very future-proof solution. So I switched to using a `RecyclerView`.

> - The search should happen with a [telephone keypad](https://en.wikipedia.org/wiki/Telephone_keypad) with the user only being able to enter numbers and each number matching multiple characters. For example, to search for either `sort` or `post`, type `7678`.  

Easily achieved by adding `android:inputType="number"` to the `EditText`. As mentioned previously, `0` and `1` are ignored in the look-up, but they still can be entered in the input form. A viable solution is to add an `OnKeyListener` to the view:
```kotlin
searchInput.setOnKeyListener { v, keyCode, event ->
    when (keyCode) {
        KeyEvent.KEYCODE_0, KeyEvent.KEYCODE_1 -> true
        else -> false
    }
}
```
However, I didn't add it, because it's not the responsibility of the view to determine whether or not a keypress is valid for the type of search we're performing.

> - The list should show all results that match by prefix. So the example above should also include `postbag`, `portability`, etc.  

A Trie turns this into a rather trivial issue. When a word is added to the dictionary, it first splits it into characters and finds its respective number on the keypad, e.g., `a` becomes `2`, `s` becomes `7` and so on. Then, starting from the root node, it looks for a child node with the appropriate key for the first character. If no node is found, it is created and added to the list of child nodes. Then it looks for the node relevant to the second character and so on. Eventually it has found its way to the node, whose key and that of its parents (except the root node) make up the given search term, e.g., `root->7->6->7->8`. The word is then added to the last node's value list.  
It is easy to see that any word, which can be prefixed by the same characters that are represented by the given digits, will be found among the children of the `8` node in this example. (I know I'm terrible with explaining things, but It Works!™)  

> - Bonus: In the results, highlight the substring by which the match was done.  

I had done this in a [previous version](https://github.com/edgars-supe/t9search/blob/77f4101099c29ec7805eb475d1cb8a248c7a4748/app/src/main/java/lv/esupe/t9search/main/MainActivity.kt#L39) of this app, but I scrapped it, because it was buggy and unclean. Since the user could input `0` and `1`, which are ignored, I had to clean up the term and send it back to the view. But I couldn't decide, whose responsibility it is to do the sanitization of the input. Obviously not the view. The ViewModel? Is the fact that `0` and `1` are ignored a part of the business logic or a detail of the dictionary implementation? No characters map to those digits, but that's how the implementation works. I thought about calling `sanitize()` before (or after) `lookup()` in the ViewModel, but then it's called twice (inside `lookup()` as well) and it just doesn't feel right.  

## Technical requirements
> `loadDictionary` should be called once when the app starts and can be slow.  

The `dictionary` property of `App` and is created and loaded in `App#onCreate()`. Then it can be acquired in the Activity by casting `applicationContext as App` and using the property, sort of a poor man's dependency injection (most Dagger tutorials I've seen do something similar to get a `DaggerAppComponent`). I did it in `App` to ensure that it the dictionary isn't loaded every time you open an Activity that uses it.  
~~The average load time is around 1600ms.~~  
After changing `ArrayList`s to `LinekdList`s, the average dictionary load time is around 350-450ms, a 4x improvement!  

> `lookup` should be fast enough to be called on the main thread without causing stutter.  
> `fun lookup(digits: String): List<String>` which returns words matching the search string. The results can be limited to a sane count.

~~For more specific queries the look-up is done in a couple of ms or even faster, but for very broad queries, e.g., just `7`, it takes around 130ms.~~ After the change to `LinkedList`s, specific queries take less than 1ms, broad queries are done in 4-30ms. I limit the shown results to 50, but I do it in the ViewModel, because then `T9Trie` is usable in cases where we need all results. 

## Updates
* Converted dictionary loading to be asynchronous using `JobIntentService`. While the dictionary is loading, a progress bar and text is shown to the user.  
* Search now accepts words with upper-case letters.  
* `MainViewModel` exposes a `LiveData` property instead of `MutableLiveData`.  
* `T9Trie`, `TrieNode` and `TrieNodeIterator` now use `LinkedList`s to store data, because adding to a `LinkedList` is much, much faster. I looked in the code for `ArrayList.addAll()` and noticed that it's really inefficient for adding items (it creates a new array each time and copies data over to it). Then I remembered about the first thing you learn in university: linked lists. It's much faster to have a node reference a new node. The result is much, much faster look-up times and greatly improved dictionary load times.  
* Made the search automatic - search as you type. The noticeable lag when entering the first digit caused me to look for more efficient implementations for lists, lead me to `LinkedList`. Removed the search button, too.  
