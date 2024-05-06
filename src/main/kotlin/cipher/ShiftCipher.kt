package cipher

import util.trimmedSplit

class ShiftCipher(
	val symbolSet: Set<String>,
	val symbolSeparator: String,
	val wordSeparator: String,
	keyCollection: Collection<String> = listOf(symbolSet.firstOrNull() ?: throw IllegalArgumentException("Symbol set can't be empty")),
	var isZeroBased: Boolean = true,
	var onConflictStrategy: OnConflictStrategy = OnConflictStrategy.Default,
) {
	private val _symbolSetAsString = symbolSet.joinToString("")


	init {
		require(keyCollection.isNotEmpty()) {
			"Key collection can't be empty"
		}
		requireValueNotInSymbolSet(symbolSeparator) {
			"Symbol separator can't be in symbol set"
		}
		requireValueNotInSymbolSet(wordSeparator) {
			"Word separator can't be in symbol set"
		}
		require(symbolSeparator != wordSeparator) {
			"Symbol separator and word separator can't be equal"
		}
		checkKeySet(keyCollection)
		checkCharIsNotPresentInBothSeparatorAndSymbolSet()
	}


	var keyCollection: Collection<String> = keyCollection
		set(value) {
			require(value.isNotEmpty()) {
				"Key collection can't be empty"
			}
			checkKeySet(value)
			field = value
		}

	private val _translatedSb = StringBuilder()
	private val _firstIndex get() = if (isZeroBased) 0 else 1


	fun encrypt(decodedText: String): String = translate(decodedText, +1)!!

	fun decrypt(encodedText: String): String = translate(encodedText, -1)!!

	fun encryptOrNull(decodedText: String): String? = translate(decodedText, +1, allowNullResult = true)

	fun decryptOrNull(encodedText: String): String? = translate(encodedText, -1, allowNullResult = true)


	private fun translate(
		textToTranslate: String,
		offsetMultiplier: Int,
		allowNullResult: Boolean = false,
	): String? {
		var wordDelimiter = ""
		var symbolDelimiter = ""
		var symbolIndex = 0

		textToTranslate.trim().split(wordSeparator).forEach { word ->
			_translatedSb.append(wordDelimiter)

			word.trimmedSplit(symbolSeparator).forEach word@{ symbol ->
				if (symbol !in symbolSet) {
					if (onConflictStrategy == OnConflictStrategy.Ignore) {
						return@word
					}
					if (allowNullResult) {
						return null
					}
					throw IllegalArgumentException("Symbol '$symbol' in text is not in symbol set $symbolSet")
				}

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

	private inline fun requireValueNotInSymbolSet(value: String, lazyMessage: () -> Any) {
		require(
			value = value !in symbolSet,
			lazyMessage = lazyMessage,
		)
	}

	private fun checkCharIsNotPresentInBothSeparatorAndSymbolSet() {
		for (char in _symbolSetAsString) {
			if (symbolSeparator != "" && char in symbolSeparator) {
				throw IllegalArgumentException("The character '$char' can't be in both symbol separator and symbol set")
			}
			if (wordSeparator != "" && char in wordSeparator) {
				throw IllegalArgumentException("The character '$char' can't be in both word separator and symbol set")
			}
		}
	}


	override fun toString(): String {
		return "ShiftCipher(symbolSeparator='$symbolSeparator', wordSeparator='$wordSeparator', keySet='$keyCollection', isZeroBased='$isZeroBased', symbolSet='$symbolSet')"
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as ShiftCipher

		if (symbolSet != other.symbolSet) return false
		if (symbolSeparator != other.symbolSeparator) return false
		if (wordSeparator != other.wordSeparator) return false
		if (isZeroBased != other.isZeroBased) return false
		if (keyCollection != other.keyCollection) return false

		return true
	}

	override fun hashCode(): Int {
		var result = symbolSet.hashCode()
		result = 31 * result + symbolSeparator.hashCode()
		result = 31 * result + wordSeparator.hashCode()
		result = 31 * result + isZeroBased.hashCode()
		result = 31 * result + keyCollection.hashCode()
		return result
	}


	enum class OnConflictStrategy {
		Ignore,
		Default,
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

		val keySet = value.replace(wordSeparator, "").trimmedSplit(symbolSeparator).toList()

		keySet.forEach { symbol ->
			if (symbol != symbolSeparator && symbol !in symbolSet) {
				throw IllegalArgumentException("Symbol '$symbol' is different from symbolSeparator '$symbolSeparator' and wordSeparator '$wordSeparator' and it's not in symbol set '$symbolSet'")
			}
		}

		this.keyCollection = keySet
	}
