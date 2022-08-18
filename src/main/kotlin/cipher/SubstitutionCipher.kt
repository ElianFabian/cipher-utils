package cipher

import Cipher
import util.splitWord

class SubstitutionCipher(
    private val encodeMap: Map<String, String>,
    private val decodedWordSeparator: String,
    private val decodedSymbolSeparator: String,
    private val encodedWordSeparator: String,
    private val encodedSymbolSeparator: String
) : Cipher
{
    private val decodeMap = encodeMap.entries.associateBy({ it.value }) { it.key }
    private val translatedSB = StringBuilder("")


    override fun encode(stringToEncode: String) = translate(
        stringToEncode,
        encodeMap,
        decodedWordSeparator,
        decodedSymbolSeparator,
        encodedWordSeparator,
        encodedSymbolSeparator,
    )

    override fun decode(stringToDecode: String) = translate(
        stringToDecode,
        decodeMap,
        encodedWordSeparator,
        encodedSymbolSeparator,
        decodedWordSeparator,
        decodedSymbolSeparator,
    )

    private fun translate(
        stringToTranslate: String,
        translationMap: Map<String, String>,
        wordSeparator: String,
        symbolSeparator: String,
        translatedWordSeparator: String,
        translatedSymbolSeparator: String
    ): String
    {
        translatedSB.setLength(0)

        var _wordSeparator = ""
        var _symbolSeparator = ""

        stringToTranslate.split(symbolSeparator + wordSeparator + symbolSeparator).forEach { word ->

            translatedSB.append(_wordSeparator)

            splitWord(word, symbolSeparator).forEach { symbol ->

                val translatedSymbol = translationMap[symbol]
                translatedSB.append(_symbolSeparator).append(translatedSymbol)

                _symbolSeparator = translatedSymbolSeparator
            }
            translatedSB.append(_symbolSeparator)
            _wordSeparator = translatedWordSeparator
        }

        if(wordSeparator == " ") translatedSB.setLength(translatedSB.length - 1)

        return translatedSB.toString()
    }
}
