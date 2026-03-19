package util

private val VOWELS = listOf('a', 'e', 'i', 'o', 'u')

/**
 * Check if the string begins with a vowel.
 * Case-insensitive, if empty or not a vowel returns false.
 * [VOWELS] are a, e, i, o, u.
 */
fun String.startsWithVowel(): Boolean {
    val firstChar = this.firstOrNull()?.lowercaseChar() ?: return false
    return VOWELS.contains(firstChar)
}