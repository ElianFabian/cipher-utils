package util

fun splitWord(string: String, symbolSeparator: String): List<String>
{
    if (symbolSeparator == "") return string.trim().toCharArray().toList().map { it.toString() }

    return string.trim().split(symbolSeparator)
}
