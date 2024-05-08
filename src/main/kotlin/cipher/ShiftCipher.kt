package cipher

import util.trimmedSplit

class ShiftCipher(
	val symbolSet: List<String>,
	val symbolSeparator: String,
	val wordSeparator: String,
	val symbolWordSeparator: String = symbolSeparator, // TODO
	keyCollection: Collection<String> = listOf(symbolSet.firstOrNull() ?: throw IllegalArgumentException("Symbol set can't be empty")),
	var isZeroBased: Boolean = true,
	var onConflictStrategy: OnConflictStrategy = OnConflictStrategy.Default,
) {
	private val _symbolSetAsString = symbolSet.joinToString("")

	private val _symbolToIndex = buildMap {
		symbolSet.forEachIndexed { index, symbol ->
			put(symbol, index)
		}
	}

	private val _indexToKeySymbol = buildMap {
		keyCollection.forEachIndexed { index, symbol ->
			put(index, symbol)
		}
	}

	init {
		require(symbolSet.none { it.isBlank() }) {
			"Blank string is not a valid symbol for symbol set"
		}
		requireValidSeparator(symbolSeparator) { char, symbol ->
			"The character '$char' from symbol separator '$symbolSeparator' was found in symbol '$symbol'"
		}
		requireValidSeparator(wordSeparator) { char, symbol ->
			"The character '$char' from word separator '$wordSeparator' was found in symbol '$symbol'"
		}
		requireValidSeparator(symbolWordSeparator) { char, symbol ->
			"The character '$char' from symbol-word separator '$symbolSeparator' was found in symbol '$symbol'"
		}
		require(symbolSeparator != wordSeparator) {
			"Symbol separator and word separator can't be equal"
		}
		if (symbolSeparator == "") {
			require(symbolSet.all { char -> char.length == 1 }) {
				"If symbol separator is an empty string all symbols in symbol set must be one single character long"
			}
		}
		requireValidKeyCollection(keyCollection)
		checkCharIsNotPresentInBothSeparatorAndSymbolSet()
	}


	var keyCollection: Collection<String> = keyCollection
		set(value) {
			requireValidKeyCollection(value)
			field = value
		}

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
		if (textToTranslate.isEmpty()) {
			return ""
		}
//		val firstChar = textToTranslate.first()
//		if (firstChar in symbolSeparator && symbolSeparator != "") {
//			throw IllegalArgumentException()
//		}
//		if (firstChar in wordSeparator && wordSeparator != "") {
//			throw IllegalArgumentException()
//		}
//		val lastChar = textToTranslate.last()
//		if (lastChar in symbolSeparator && symbolSeparator != "") {
//			throw IllegalArgumentException()
//		}
//		if (lastChar in wordSeparator && wordSeparator != "") {
//			throw IllegalArgumentException()
//		}


		// h.e.l.l.o. .w.o.r.l.d
		// .h.e..l.l.o . w.o.r.l.d.
		// alpha::beta.->.delta

		val firstChar = textToTranslate.first()
		if (firstChar !in _symbolSetAsString) {
			if (allowNullResult) {
				return null
			}
			else throw IllegalArgumentException("First character must always be one from a symbol")
		}

		var symbolIndex = 0

		val translatedResultSb = StringBuilder()
		// segments: symbols and separators
		val currentSegment = StringBuilder()

		var previousCharType: Int = -1

		val sequence = sequence {
			yieldAll(textToTranslate.asSequence())
			yield(Char.MIN_VALUE)
		}

		for ((index, currentChar) in sequence.withIndex()) {

			val currentCharType = when (currentChar) {
				in symbolSeparator -> TYPE_SYMBOL_SEPARATOR
				in wordSeparator -> TYPE_WORD_SEPARATOR
				in symbolWordSeparator -> TYPE_SYMBOL_WORD_SEPARATOR
				in _symbolSetAsString -> TYPE_SYMBOL
				else -> -1
			}

			if (currentCharType != previousCharType && previousCharType > -1) {
				if (index == textToTranslate.lastIndex) {
					currentSegment.append(currentChar)
				}
				val segment = currentSegment.toString()
				currentSegment.clear()

				when (previousCharType) {
					TYPE_SYMBOL -> {
						val cycleIndex = symbolIndex.mod(keyCollection.size)
						//val offset = symbolSet.indexOf(keyCollection.elementAt(cycleIndex)) + _firstIndex
						val offset = _symbolToIndex[_indexToKeySymbol[cycleIndex]]!! + _firstIndex
						val translatedSymbol = shift(segment, offset * offsetMultiplier)

						translatedResultSb.append(translatedSymbol)

						symbolIndex++
					}
					TYPE_SYMBOL_SEPARATOR -> {
						translatedResultSb.append(symbolSeparator)
					}
					TYPE_WORD_SEPARATOR -> {
						translatedResultSb.append(wordSeparator)
					}
					TYPE_SYMBOL_WORD_SEPARATOR -> {
						translatedResultSb.append(symbolWordSeparator)
					}
				}
			}


			currentSegment.append(currentChar)

			previousCharType = currentCharType


//			if (currentCharType == TYPE_SYMBOL && symbolSeparator == "") {
//				val segment = currentChar.toString()
//				val cycleIndex = symbolIndex.mod(keyCollection.size)
//				val offset = _symbolToIndex[_indexToKeySymbol[cycleIndex]]!! + _firstIndex
//				val translatedSymbol = shift(segment, offset * offsetMultiplier)
//
//				translatedResultSb.append(translatedSymbol)
//
//				symbolIndex++
//			}

		}


//		textToTranslate.trim().split(wordSeparator).forEach { word ->
//			translatedResultSb.append(wordSeparatorAux)
//
//			word.trimmedSplit(symbolSeparator).forEach word@{ symbol ->
//				if (symbol !in symbolSet) {
//					if (onConflictStrategy == OnConflictStrategy.Ignore) {
//						return@word
//					}
//					if (allowNullResult) {
//						return null
//					}
//					throw IllegalArgumentException("Symbol '$symbol' in text is not in symbol set $symbolSet")
//				}
//
//				val cycleIndex = symbolIndex.mod(keyCollection.size)
//				val offset = symbolSet.indexOf(keyCollection.elementAt(cycleIndex)) + _firstIndex
//				val translatedSymbol = shift(symbol, offset * offsetMultiplier)
//
//				translatedResultSb.append(symbolSeparatorAux).append(translatedSymbol)
//
//				symbolSeparatorAux = symbolSeparator
//				symbolIndex++
//			}
//			translatedResultSb.append(symbolSeparatorAux)
//			wordSeparatorAux = wordSeparator
//		}

		val result = translatedResultSb.toString()
		//translatedResultSb.setLength(0)

		return result
	}

	private fun shift(symbol: String, offset: Int): String {
		val oldIndex = _symbolToIndex[symbol] ?: -1
		val newIndex = (oldIndex + offset).mod(symbolSet.size)

		return symbolSet[newIndex]
	}

	private fun requireValidKeyCollection(keyCollection: Collection<String>) {
		require(keyCollection.isNotEmpty()) {
			"Key collection can't be empty"
		}
		keyCollection.forEach { symbol ->
			if (wordSeparator in symbol) {
				throw IllegalArgumentException("Symbol '$symbol' in key set can't contain the wordSeparator symbol")
			}
			if (symbol !in symbolSet) {
				throw IllegalArgumentException("Symbol '$symbol' not found in symbol set $symbolSet")
			}
		}
	}

	private inline fun requireValidSeparator(value: String, lazyMessage: (char: Char, symbol: String) -> Any) {
		for (symbol in symbolSet) {
			value.forEach { char ->
				if (char in symbol) {
					throw IllegalArgumentException(lazyMessage(char, symbol).toString())
				}
			}
		}
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
		return "ShiftCipher(symbolSeparator='$symbolSeparator', wordSeparator='$wordSeparator', symbolWordSeparator='$symbolWordSeparator', keySet='$keyCollection', isZeroBased='$isZeroBased', symbolSet='$symbolSet')"
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as ShiftCipher

		if (symbolSet != other.symbolSet) return false
		if (symbolSeparator != other.symbolSeparator) return false
		if (wordSeparator != other.wordSeparator) return false
		if (symbolWordSeparator != other.symbolWordSeparator) return false
		if (isZeroBased != other.isZeroBased) return false
		if (keyCollection != other.keyCollection) return false

		return true
	}

	override fun hashCode(): Int {
		var result = symbolSet.hashCode()
		result = 31 * result + symbolSeparator.hashCode()
		result = 31 * result + wordSeparator.hashCode()
		result = 31 * result + symbolWordSeparator.hashCode()
		result = 31 * result + isZeroBased.hashCode()
		result = 31 * result + keyCollection.hashCode()
		return result
	}


	enum class OnConflictStrategy {
		Ignore,
		Default,
	}


	companion object {
		const val TYPE_SYMBOL = 0
		const val TYPE_SYMBOL_SEPARATOR = 1
		const val TYPE_WORD_SEPARATOR = 2
		const val TYPE_SYMBOL_WORD_SEPARATOR = 3
	}
}


var ShiftCipher.key: String
	get() {
		return keyCollection.joinToString(symbolSeparator)
	}
	set(value) {
		if (value.isBlank()) {
			throw IllegalArgumentException("Key can't be blank")
		}

		val keySet = value.replace(wordSeparator, "").trimmedSplit(symbolSeparator).toList()

		keySet.forEach { symbol ->
			if (symbol != symbolSeparator && symbol !in symbolSet) {
				throw IllegalArgumentException("Symbol '$symbol' is different from symbol separator '$symbolSeparator' and word separator '$wordSeparator' and it's not in symbol set '$symbolSet'")
			}
		}

		this.keyCollection = keySet
	}
