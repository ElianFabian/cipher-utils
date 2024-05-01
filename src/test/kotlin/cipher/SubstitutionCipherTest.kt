package cipher

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class SubstitutionCipherTest {

	private lateinit var morseCipher: SubstitutionCipher
	private lateinit var gboard1Cipher: SubstitutionCipher

	private val toMorseMap = mapOf(
		"a" to ".-",
		"b" to "-...",
		"c" to "-.-.",
		"d" to "-..",
		"e" to ".",
		"f" to "..-.",
		"g" to "--.",
		"h" to "....",
		"i" to "..",
		"j" to ".---",
		"k" to "-.-",
		"l" to ".-..",
		"m" to "--",
		"n" to "-.",
		"ñ" to "--.--",
		"o" to "---",
		"p" to ".--.",
		"q" to "--.-",
		"r" to ".-.",
		"s" to "...",
		"t" to "-",
		"u" to "..-",
		"v" to "...-",
		"w" to ".--",
		"x" to "-..-",
		"y" to "-.--",
		"z" to "--..",
	)

	private val toGboard1 = mapOf(
		"a" to "@",
		"b" to ";",
		"c" to "'",
		"d" to "€",
		"e" to "3",
		"f" to "_",
		"g" to "&",
		"h" to "-",
		"i" to "8",
		"j" to "+",
		"k" to "(",
		"l" to ")",
		"m" to "?",
		"n" to "!",
		"ñ" to "/",
		"o" to "9",
		"p" to "0",
		"q" to "1",
		"r" to "4",
		"s" to "#",
		"t" to "5",
		"u" to "7",
		"v" to ":",
		"w" to "2",
		"x" to "\"",
		"y" to "6",
		"z" to "*",
	)

	private val naturalString = "abcdefghijklmnñopqrstuvwxyz and oxofempal"
	private val morseString = ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --.-- --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --.. / .- -. -.. / --- -..- --- ..-. . -- .--. .- .-.."
	private val gboard1String = "@;'€3_&-8+()?!/9014#57:2\"6* @!€ 9\"9_3?0@)"


	@BeforeEach
	fun onBefore() {
		morseCipher = SubstitutionCipher(
			encodeMap = toMorseMap,
			decodedWordSeparator = " ",
			decodedSymbolSeparator = "",
			encodedWordSeparator = "/",
			encodedSymbolSeparator = " ",
		)

		gboard1Cipher = SubstitutionCipher(
			encodeMap = toGboard1,
			decodedWordSeparator = " ",
			decodedSymbolSeparator = "",
			encodedWordSeparator = " ",
			encodedSymbolSeparator = "",
		)
	}

	@Test
	fun `Morse - Decrypt an encoded string must be equal to the original one`() {
		val encodedString = morseCipher.encrypt(naturalString)
		assertThat(encodedString).isNotNull()
		val decodedString = morseCipher.decrypt(encodedString!!)

		assertThat(decodedString).isEqualTo(naturalString)
	}

	@Test
	fun `Morse - Encrypt is correct`() {
		val encodedString = morseCipher.encrypt(naturalString)

		assertThat(encodedString).isEqualTo(morseString)
	}

	@Test
	fun `Morse - Decrypt is correct`() {
		val decodedString = morseCipher.decrypt(morseString)

		assertThat(decodedString).isEqualTo(naturalString)
	}

	@Test
	fun `GBoard1 - Decrypt an encoded string must be equal to the original one`() {
		val encodedString = gboard1Cipher.encrypt(naturalString)
		assertThat(encodedString).isNotNull()
		val decodedString = gboard1Cipher.decrypt(encodedString!!)

		assertThat(decodedString).isEqualTo(naturalString)
	}

	@Test
	fun `GBoard1 - Encrypt is correct`() {
		val encodedString = gboard1Cipher.encrypt(naturalString)

		assertThat(encodedString).isEqualTo(gboard1String)
	}

	@Test
	fun `GBoard1 - Decrypt is correct`() {
		val decodedString = gboard1Cipher.decrypt(gboard1String)

		assertThat(decodedString).isEqualTo(naturalString)
	}
}
