package cipher

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class ShiftCipherTest {

	private lateinit var morseCipher: ShiftCipher
	private lateinit var alphabetCipher: ShiftCipher

	private val morseSet = setOf(
		".-",
		"-...",
		"-.-.",
		"-..",
		".",
		"..-.",
		"--.",
		"....",
		"..",
		".---",
		"-.-",
		".-..",
		"--",
		"-.",
		"--.--",
		"---",
		".--.",
		"--.-",
		".-.",
		"...",
		"-",
		"..-",
		"...-",
		".--",
		"-..-",
		"-.--",
		"--..",
	)

	private val alphabetSet = setOf(
		"a",
		"b",
		"c",
		"d",
		"e",
		"f",
		"g",
		"h",
		"i",
		"j",
		"k",
		"l",
		"m",
		"n",
		"ñ",
		"o",
		"p",
		"q",
		"r",
		"s",
		"t",
		"u",
		"v",
		"w",
		"x",
		"y",
		"z",
	)

	private val morseString = ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --.-- --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --.. / .- -. -.. / --- -..- --- ..-. . -- .--. .- .-.."
	private val shiftedMorseString = "-... -.. ..-. . --. .. .... .--- .-.. -.- -- --.-- -. --- --.- .--. .-. - ... ..- .-- ...- -..- --.. -.-- .- -.-. / -... --- --. / .--. --.. .-. --. --. --- --.- -.-. --.--"

	private val alphabetString = "abcdefghijklmnñopqrstuvwxyz and oxofempal"
	private val shiftedAlphabetString = "bdfegihjlkmñnoqprtsuwvxzyac bog pzrggoqcñ"


	@BeforeEach
	fun onBefore() {
		morseCipher = ShiftCipher(
			symbolSet = morseSet,
			wordSeparator = "/",
			symbolSeparator = " ",
			keyCollection = listOf(
				".-",
				"-...",
				"-.-.",
			),
			isZeroBased = false,
		)

		alphabetCipher = ShiftCipher(
			symbolSet = alphabetSet,
			wordSeparator = " ",
			symbolSeparator = "",
			keyCollection = listOf(
				"a",
				"b",
				"c",
			),
			isZeroBased = false,
		)
	}

	@Test
	fun `Morse - Decrypt an encoded string must be equal to the original one`() {
		val encodedString = morseCipher.encrypt(morseString)
		val decodedString = morseCipher.decrypt(encodedString)

		assertThat(decodedString).isEqualTo(morseString)
	}

	@Test
	fun `Morse - Encrypt is correct`() {
		val encodedString = morseCipher.encrypt(morseString)

		assertThat(encodedString).isEqualTo(shiftedMorseString)
	}

	@Test
	fun `Morse - Decrypt is correct`() {
		val decodedString = morseCipher.decrypt(shiftedMorseString)

		assertThat(decodedString).isEqualTo(morseString)
	}

	@Test
	fun `Alphabet - Decrypt an encoded string must be equal to the original one`() {
		val encodedString = alphabetCipher.encrypt(alphabetString)
		val decodedString = alphabetCipher.decrypt(encodedString)

		assertThat(decodedString).isEqualTo(alphabetString)
	}

	@Test
	fun `Alphabet - Encrypt is correct`() {
		val encodedString = alphabetCipher.encrypt(alphabetString)

		assertThat(encodedString).isEqualTo(shiftedAlphabetString)
	}

	@Test
	fun `Alphabet - Decrypt is correct`() {
		val decodedString = alphabetCipher.decrypt(shiftedAlphabetString)

		assertThat(decodedString).isEqualTo(alphabetString)
	}
}
