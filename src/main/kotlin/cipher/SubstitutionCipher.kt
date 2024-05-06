package cipher

import util.trimmedSplit

class SubstitutionCipher(
	val encryptMap: Map<String, String>,
	val wordSeparator: String,
	val symbolSeparator: String,
	val encryptedSymbolSeparator: String,
	val encryptedWordSeparator: String,
) {
	init {
		check(encryptMap.isNotEmpty()) {
			"Encode map set can't be empty"
		}
		checkValueNotInMap(wordSeparator) {
			"decodedWordSeparator can't be in encryptMap"
		}
		checkValueNotInMap(symbolSeparator) {
			"decodedSymbolSeparator can't be in encryptMap"
		}
		checkValueNotInMap(encryptedWordSeparator) {
			"encodedWordSeparator can't be in encryptMap"
		}
		checkValueNotInMap(encryptedSymbolSeparator) {
			"encodedSymbolSeparator can't be in encryptMap"
		}
	}


	private val _decryptMap = encryptMap.entries.associateBy({ it.value }) { it.key }
	private val _translatedSb = StringBuilder("")


	fun encrypt(decodedText: String): String = translate(
		textToTranslate = decodedText,
		translationMap = encryptMap,
		symbolSeparator = symbolSeparator,
		wordSeparator = wordSeparator,
		translatedWordSeparator = encryptedWordSeparator,
		translatedSymbolSeparator = encryptedSymbolSeparator,
	)

	fun decrypt(encodedText: String): String = translate(
		textToTranslate = encodedText,
		translationMap = _decryptMap,
		symbolSeparator = encryptedSymbolSeparator,
		wordSeparator = encryptedWordSeparator,
		translatedWordSeparator = wordSeparator,
		translatedSymbolSeparator = symbolSeparator,
	)


	private fun translate(
		textToTranslate: String,
		translationMap: Map<String, String>,
		symbolSeparator: String,
		wordSeparator: String,
		translatedWordSeparator: String,
		translatedSymbolSeparator: String,
	): String {
		var wordDelimiter = ""
		var symbolDelimiter = ""

		textToTranslate.trim().split(wordSeparator).forEach { word ->
			_translatedSb.append(wordDelimiter)

			word.trimmedSplit(symbolSeparator).forEach { symbol ->
				val translatedSymbol = translationMap[symbol] ?: run {
					_translatedSb.setLength(0)
					throw IllegalArgumentException("Symbol '$symbol' is not in encryptMap $encryptMap")
				}

				_translatedSb.append(symbolDelimiter).append(translatedSymbol)

				symbolDelimiter = translatedSymbolSeparator
			}
			_translatedSb.append(symbolDelimiter)
			wordDelimiter = translatedWordSeparator
		}

		val result = _translatedSb.toString().trim()
		_translatedSb.setLength(0)

		return result
	}

	private inline fun checkValueNotInMap(value: String, lazyMessage: () -> Any) {
		check(
			value = value !in encryptMap && value !in encryptMap.values,
			lazyMessage = lazyMessage,
		)
	}


	override fun toString(): String {
		return "SubstitutionCipher(symbolSeparator='$symbolSeparator', wordSeparator='$wordSeparator', decryptedWordSeparator='$encryptedWordSeparator', decryptedSymbolSeparator='$encryptedSymbolSeparator, encryptMap='$encryptMap')"
	}


	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as SubstitutionCipher

		if (encryptMap != other.encryptMap) return false
		if (symbolSeparator != other.symbolSeparator) return false
		if (wordSeparator != other.wordSeparator) return false
		if (encryptedWordSeparator != other.encryptedWordSeparator) return false
		if (encryptedSymbolSeparator != other.encryptedSymbolSeparator) return false

		return true
	}

	override fun hashCode(): Int {
		var result = encryptMap.hashCode()
		result = 31 * result + symbolSeparator.hashCode()
		result = 31 * result + wordSeparator.hashCode()
		result = 31 * result + encryptedWordSeparator.hashCode()
		result = 31 * result + encryptedSymbolSeparator.hashCode()
		return result
	}
}
