package cipher

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class ShiftCipherTest
{
    private lateinit var morseCipher: ShiftCipher
    private lateinit var alphabetCipher: ShiftCipher

    private val morseSet = setOf(
        ".-", "-...", "-.-.", "-..", ".",
        "..-.", "--.", "....", "..", ".---",
        "-.-", ".-..", "--", "-.", "--.--",
        "---", ".--.", "--.-", ".-.", "...",
        "-", "..-", "...-", ".--", "-..-",
        "-.--", "--.."
    )

    private val alphabetSet = setOf(
        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
        "k", "l", "m", "n", "ñ", "o", "p", "q", "r", "s", "t",
        "u", "v", "w", "x", "y", "z"
    )

    private val morseString = ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --.-- --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --.. / .- -. -.. / --- -..- --- ..-. . -- .--. .- .-.."
    private val shiftedMorseString = "-... -.. ..-. . --. .. .... .--- .-.. -.- -- --.-- -. --- --.- .--. .-. - ... ..- .-- ...- -..- --.. -.-- .- -.-. / -... --- --. / .--. --.. .-. --. --. --- --.- -.-. --.--"

    private val alphabetString = "abcdefghijklmnñopqrstuvwxyz and oxofempal"
    private val shiftedAlphabetString = "bdfegihjlkmñnoqprtsuwvxzyac bog pzrggoqcñ"

    @BeforeEach
    fun onBefore()
    {
        morseCipher = ShiftCipher(
            morseSet,
            wordSeparator = "/",
            symbolSeparator = " ",
            cipherKey = ".- -... -.-."
        )

        alphabetCipher = ShiftCipher(
            alphabetSet,
            wordSeparator = " ",
            symbolSeparator = "",
            cipherKey = "abc"
        )
    }

    @Test
    fun `Morse - Decode an encoded string must be equal to the original one`()
    {
        val encodedString = morseCipher.encode(morseString)
        val decodedString = morseCipher.decode(encodedString)

        assertThat(decodedString).isEqualTo(morseString)
    }

    @Test
    fun `Morse - Encode is correct`()
    {
        val encodedString = morseCipher.encode(morseString)

        assertThat(encodedString).isEqualTo(shiftedMorseString)
    }

    @Test
    fun `Morse - Decode is correct`()
    {
        val decodedString = morseCipher.decode(shiftedMorseString)

        assertThat(decodedString).isEqualTo(morseString)
    }

    @Test
    fun `Alphabet - Decode an encoded string must be equal to the original one`()
    {
        val encodedString = alphabetCipher.encode(alphabetString)
        val decodedString = alphabetCipher.decode(encodedString)

        assertThat(decodedString).isEqualTo(alphabetString)
    }

    @Test
    fun `Alphabet - Encode is correct`()
    {
        val encodedString = alphabetCipher.encode(alphabetString)

        assertThat(encodedString).isEqualTo(shiftedAlphabetString)
    }

    @Test
    fun `Alphabet - Decode is correct`()
    {
        val decodedString = alphabetCipher.decode(shiftedAlphabetString)

        assertThat(decodedString).isEqualTo(alphabetString)
    }
}
