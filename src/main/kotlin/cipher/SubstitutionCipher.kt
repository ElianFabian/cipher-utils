package cipher

import util.trimmedSplit

class SubstitutionCipher(
	val encodeMap: Map<String, String>,
	val decodedWordSeparator: String,
	val decodedSymbolSeparator: String,
	val encodedWordSeparator: String,
	val encodedSymbolSeparator: String,
) {
	init {
		checkValueNotInMap(decodedWordSeparator) {
			"decodedWordSeparator can't be in encodeMap"
		}
		checkValueNotInMap(decodedSymbolSeparator) {
			"decodedSymbolSeparator can't be in encodeMap"
		}
		checkValueNotInMap(encodedWordSeparator) {
			"encodedWordSeparator can't be in encodeMap"
		}
		checkValueNotInMap(encodedSymbolSeparator) {
			"encodedSymbolSeparator can't be in encodeMap"
		}
	}


	private val _decodeMap = encodeMap.entries.associateBy({ it.value }) { it.key }
	private val _translatedSb = StringBuilder("")


	fun encrypt(decodedText: String): String? = translate(
		textToTranslate = decodedText,
		translationMap = encodeMap,
		wordSeparator = decodedWordSeparator,
		symbolSeparator = decodedSymbolSeparator,
		translatedWordSeparator = encodedWordSeparator,
		translatedSymbolSeparator = encodedSymbolSeparator,
	)

	fun decrypt(encodedText: String): String? = translate(
		textToTranslate = encodedText,
		translationMap = _decodeMap,
		wordSeparator = encodedWordSeparator,
		symbolSeparator = encodedSymbolSeparator,
		translatedWordSeparator = decodedWordSeparator,
		translatedSymbolSeparator = decodedSymbolSeparator,
	)


	private fun translate(
		textToTranslate: String,
		translationMap: Map<String, String>,
		wordSeparator: String,
		symbolSeparator: String,
		translatedWordSeparator: String,
		translatedSymbolSeparator: String,
	): String? {
		var wordDelimiter = ""
		var symbolDelimiter = ""

		textToTranslate.trim().split(wordSeparator).forEach { word ->
			_translatedSb.append(wordDelimiter)

			word.trimmedSplit(symbolSeparator).forEach { symbol ->
				val translatedSymbol = translationMap[symbol] ?: run {
					_translatedSb.setLength(0)
					return null
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
			value = value !in encodeMap && value !in encodeMap.values,
			lazyMessage = lazyMessage,
		)
	}


	override fun toString(): String {
		return "SubstitutionCipher(decodedWordSeparator='$decodedWordSeparator', decodedSymbolSeparator='$decodedSymbolSeparator', encodedWordSeparator='$encodedWordSeparator', encodedSymbolSeparator='$encodedSymbolSeparator, encodeMap='$encodeMap')"
	}


	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as SubstitutionCipher

		if (encodeMap != other.encodeMap) return false
		if (decodedWordSeparator != other.decodedWordSeparator) return false
		if (decodedSymbolSeparator != other.decodedSymbolSeparator) return false
		if (encodedWordSeparator != other.encodedWordSeparator) return false
		if (encodedSymbolSeparator != other.encodedSymbolSeparator) return false

		return true
	}

	override fun hashCode(): Int {
		var result = encodeMap.hashCode()
		result = 31 * result + decodedWordSeparator.hashCode()
		result = 31 * result + decodedSymbolSeparator.hashCode()
		result = 31 * result + encodedWordSeparator.hashCode()
		result = 31 * result + encodedSymbolSeparator.hashCode()
		return result
	}
}
