package cipher

import util.trimmedSplit

class ShiftCipher(
	val symbolSet: Set<String>,
	val wordSeparator: String,
	val symbolSeparator: String,
	keyCollection: Collection<String> = listOf(symbolSet.first()),
	var isZeroBased: Boolean = true,
) {
	init {
		checkValueNotInSymbolSet(wordSeparator) {
			"wordSeparator can't be in symbol set"
		}
		checkValueNotInSymbolSet(symbolSeparator) {
			"symbolSeparator can't be in symbol set"
		}
		checkKeySet(keyCollection)
	}


	var keyCollection: Collection<String> = keyCollection
		set(value) {
			checkKeySet(value)
			field = value
		}

	private val _translatedSb = StringBuilder("")
	private val _firstIndex get() = if (isZeroBased) 0 else 1


	fun encrypt(decodedText: String): String = translate(decodedText, +1)

	fun decrypt(encodedText: String): String = translate(encodedText, -1)


	private fun translate(
		stringToTranslate: String,
		offsetMultiplier: Int,
	): String {
		var wordDelimiter = ""
		var symbolDelimiter = ""
		var symbolIndex = 0

		stringToTranslate.trim().split(wordSeparator).forEach { word ->
			_translatedSb.append(wordDelimiter)

			word.trimmedSplit(symbolSeparator).forEach { symbol ->
				val cycleIndex = symbolIndex.mod(keyCollection.size)
				val offset = symbolSet.indexOf(keyCollection.elementAt(cycleIndex)) + _firstIndex
				val translatedSymbol = shift(symbol, offset * offsetMultiplier)

				_translatedSb.append(symbolDelimiter).append(translatedSymbol)

				symbolDelimiter = symbolSeparator
				symbolIndex++
			}
			_translatedSb.append(symbolDelimiter)
			wordDelimiter = wordSeparator
		}

		val result = _translatedSb.toString().trim()
		_translatedSb.setLength(0)

		return result
	}

	private fun shift(symbol: String, offset: Int): String {
		val oldIndex = symbolSet.indexOf(symbol)
		val newIndex = (oldIndex + offset).mod(symbolSet.size)

		return symbolSet.elementAt(newIndex)
	}

	private fun checkKeySet(keyCollection: Collection<String>) {
		keyCollection.forEach { symbol ->
			if (wordSeparator in symbol) {
				throw IllegalArgumentException("Symbol '$symbol' in key set can't contain the wordSeparator symbol")
			}
			if (symbol !in symbolSet) {
				throw IllegalArgumentException("Symbol '$symbol' not found in symbol set $symbolSet")
			}
		}
	}

	private inline fun checkValueNotInSymbolSet(value: String, lazyMessage: () -> Any) {
		check(
			value = value !in symbolSet,
			lazyMessage = lazyMessage,
		)
	}


	override fun toString(): String {
		return "ShiftCipher(wordSeparator='$wordSeparator', symbolSeparator='$symbolSeparator', keySet='$keyCollection', isZeroBased='$isZeroBased', symbolSet='$symbolSet')"
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as ShiftCipher

		if (symbolSet != other.symbolSet) return false
		if (wordSeparator != other.wordSeparator) return false
		if (symbolSeparator != other.symbolSeparator) return false
		if (isZeroBased != other.isZeroBased) return false
		if (keyCollection != other.keyCollection) return false

		return true
	}

	override fun hashCode(): Int {
		var result = symbolSet.hashCode()
		result = 31 * result + wordSeparator.hashCode()
		result = 31 * result + symbolSeparator.hashCode()
		result = 31 * result + isZeroBased.hashCode()
		result = 31 * result + keyCollection.hashCode()
		return result
	}
}

var ShiftCipher.key: String
	get() {
		return keyCollection.joinToString(symbolSeparator)
	}
	set(value) {
		if (value.isBlank()) {
			throw IllegalArgumentException("key can't be blank")
		}

		val keySet = value.trimmedSplit(symbolSeparator).toList()

		keySet.forEach { symbol ->
			if (symbol != symbolSeparator && symbolSeparator != wordSeparator && symbol !in symbolSet) {
				throw IllegalArgumentException("Symbol '$symbol' is different from symbolSeparator '$symbolSeparator' and wordSeparator '$wordSeparator' and it's not in symbol set '$symbolSet'")
			}
		}

		this.keyCollection = keySet
	}
