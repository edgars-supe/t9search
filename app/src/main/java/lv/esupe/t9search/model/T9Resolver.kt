package lv.esupe.t9search.model


/**
 * A T9 resolver for a Trie tree. Maps keys to their respective digits on a phone keypad, i.e.,
 * 'a', 'b', 'c' => '2'; 'd', 'e', 'f' => '3' and so on.
 */
class T9Resolver : ValueKeyResolver {
    private val keyValues: HashMap<Char, Char> = HashMap()

    init {
        keyValues['a'] = '2'
        keyValues['b'] = '2'
        keyValues['c'] = '2'
        keyValues['d'] = '3'
        keyValues['e'] = '3'
        keyValues['f'] = '3'
        keyValues['g'] = '4'
        keyValues['h'] = '4'
        keyValues['i'] = '4'
        keyValues['j'] = '5'
        keyValues['k'] = '5'
        keyValues['l'] = '5'
        keyValues['m'] = '6'
        keyValues['n'] = '6'
        keyValues['o'] = '6'
        keyValues['p'] = '7'
        keyValues['q'] = '7'
        keyValues['r'] = '7'
        keyValues['s'] = '7'
        keyValues['t'] = '8'
        keyValues['u'] = '8'
        keyValues['v'] = '8'
        keyValues['w'] = '9'
        keyValues['x'] = '9'
        keyValues['y'] = '9'
        keyValues['z'] = '9'
    }

    override fun resolve(input: String): List<Char> {
        return input.mapNotNull { char ->
            keyValues[char]
        }
    }
}