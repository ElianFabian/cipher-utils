package cipher

import cipher.data.AlphabetSet
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import kotlin.test.assertFailsWith

internal class ShiftCipherTest {

	private lateinit var alphabetCipher: ShiftCipher


	@BeforeEach
	fun onBefore() {
		alphabetCipher = ShiftCipher(
			symbolSet = AlphabetSet,
			wordSeparator = " ",
			symbolSeparator = "",
		)
	}

	@Test
	fun `Symbol set can't be empty`() {
		assertFailsWith<IllegalArgumentException> {
			ShiftCipher(
				symbolSet = emptyList(),
				symbolSeparator = "",
				wordSeparator = " ",
			)
		}
	}

	@Test
	fun `Key collection can't be empty`() {
		assertFailsWith<IllegalArgumentException> {
			ShiftCipher(
				symbolSet = AlphabetSet,
				symbolSeparator = "",
				wordSeparator = " ",
				keyCollection = emptyList(),
			)
		}
		assertFailsWith<IllegalArgumentException> {
			val cipher = ShiftCipher(
				symbolSet = AlphabetSet,
				symbolSeparator = "",
				wordSeparator = " ",
			)
			cipher.keyCollection = emptyList()
		}
	}

	@Test
	fun `Symbol separator can't be equal to word separator`() {
		val symbolSeparators = setOf(
			"", ".", ",", "_", ":", "/"
		)
		symbolSeparators.forEach { symbol ->
			assertFailsWith<IllegalArgumentException> {
				ShiftCipher(
					symbolSet = AlphabetSet,
					symbolSeparator = symbol,
					wordSeparator = symbol,
				)
			}
		}
	}

	@Test
	fun `Symbol separator can't be in symbol set`() {
		val symbolSeparators = setOf(
			"", ".", ",", "_", ":"
		)
		symbolSeparators.forEach { symbol ->
			assertFailsWith<IllegalArgumentException> {
				ShiftCipher(
					symbolSet = AlphabetSet + symbol,
					symbolSeparator = symbol,
					wordSeparator = "/",
				)
			}
		}
	}

	@Test
	fun `Word separator can't be in symbol set`() {
		val wordSeparators = setOf(
			"", ".", ",", "_", ":"
		)
		wordSeparators.forEach { symbol ->
			assertFailsWith<IllegalArgumentException> {
				ShiftCipher(
					symbolSet = AlphabetSet + symbol,
					symbolSeparator = "/",
					wordSeparator = symbol,
				)
			}
		}
	}

	@Test
	fun `On conflict strategy works as expected`() {
		val cipher = ShiftCipher(
			symbolSet = AlphabetSet,
			symbolSeparator = "",
			wordSeparator = " ",
			keyCollection = listOf("a"),
		)

		cipher.onConflictStrategy = ShiftCipher.OnConflictStrategy.Default

		val text = "hello world with Ñ"

		assertFailsWith<IllegalArgumentException> {
			cipher.encrypt(text)
		}
		assertThat(cipher.encryptOrNull(text)).isNull()

		assertFailsWith<IllegalArgumentException> {
			cipher.decrypt(text)
		}
		assertThat(cipher.decryptOrNull(text)).isNull()


		cipher.onConflictStrategy = ShiftCipher.OnConflictStrategy.Ignore

		assertDoesNotThrow {
			cipher.encrypt(text)
		}
		assertThat(cipher.encryptOrNull(text)).isNotNull()

		assertDoesNotThrow {
			cipher.decrypt(text)
		}
		assertThat(cipher.decryptOrNull(text)).isNotNull()
	}

	@Test
	fun `Character can't be in both symbol separator and symbol set`() {
		val set = listOf("alpha", "beta", "delta")
		val characters = set.joinToString("")

		characters.forEach { char ->
			assertFailsWith<IllegalArgumentException> {
				ShiftCipher(
					symbolSet = set,
					symbolSeparator = "<$char>",
					wordSeparator = ".",
				)
			}
		}
	}

	@Test
	fun `Character can't be in both word separator and symbol set`() {
		val set = listOf("alpha", "beta", "delta")
		val characters = set.joinToString("")

		characters.forEach { char ->
			assertFailsWith<IllegalArgumentException> {
				ShiftCipher(
					symbolSet = set,
					symbolSeparator = ".",
					wordSeparator = "<$char>",
				)
			}
		}
	}

	@CsvFileSource(
		resources = [
			"/shift-cipher-alphabet-data.csv",
		],
		numLinesToSkip = 1,
	)
	@ParameterizedTest
	fun `Alphabet - Expected value matches actual value and encryption can be reversed`(
		key: String,
		sourceText: String,
		expectedEncryptedText: String,
		isZeroBased: Boolean,
	) {
		alphabetCipher.apply {
			this.key = key
			this.isZeroBased = isZeroBased
		}

		val encryptedText = alphabetCipher.encrypt(sourceText)
		assertThat(encryptedText).isEqualTo(expectedEncryptedText)

		val decryptedText = alphabetCipher.decrypt(encryptedText)
		assertThat(decryptedText).isEqualTo(sourceText)
	}
}
