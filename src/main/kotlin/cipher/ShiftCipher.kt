package cipher

import Cipher
import util.mod
import util.splitWord

class ShiftCipher(
    private val symbolSet: Set<String>,
    private val wordSeparator: String,
    private val symbolSeparator: String,
    cipherKey: String = symbolSet.elementAt(0),
    firstIndexType: FirstIndexType = FirstIndexType.ONE
) : Cipher
{
    var key = cipherKey
        set(value)
        {
            keySet = splitWord(value, symbolSeparator).toSet()
            field = value
        }

    private var keySet: Set<String> =
        if (cipherKey.isEmpty()) splitWord(symbolSet.elementAt(0), symbolSeparator).toSet()
        else splitWord(cipherKey, symbolSeparator).toSet()

    private val translatedSB = StringBuilder("")
    private val firstIndex = if (firstIndexType == FirstIndexType.ZERO) 0 else 1

    override fun encode(stringToEncode: String) = translate(stringToEncode)

    override fun decode(stringToDecode: String) = translate(stringToDecode, -1)

    private fun translate(stringToTranslate: String, offsetMultiplier: Int = 1): String
    {
        translatedSB.setLength(0)

        var _wordSeparator = ""
        var _symbolSeparator = ""
        var symbolIndex = 0

        stringToTranslate.trim().split(symbolSeparator + wordSeparator + symbolSeparator).forEach { word ->

            translatedSB.append(_wordSeparator)

            splitWord(word, symbolSeparator).forEach { symbol ->

                val cycleIndex = mod(symbolIndex, keySet.size)
                val offset = symbolSet.indexOf(keySet.elementAt(cycleIndex)) + firstIndex
                val translatedSymbol = shift(symbol, offset * offsetMultiplier)

                translatedSB.append(_symbolSeparator).append(translatedSymbol)

                _symbolSeparator = symbolSeparator
                symbolIndex++
            }
            translatedSB.append(_symbolSeparator)
            _wordSeparator = wordSeparator
        }

        return translatedSB.toString().trim()
    }

    private fun shift(symbol: String, offset: Int): String
    {
        val oldIndex = symbolSet.indexOf(symbol)
        val newIndex = mod(oldIndex + offset, symbolSet.size)

        return symbolSet.elementAt(newIndex)
    }
}

enum class FirstIndexType
{
    ZERO, ONE
}