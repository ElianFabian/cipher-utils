package cipher

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class ShiftCipherTest
{
    private lateinit var alphabetCipher: ShiftCipher
    private lateinit var morseCipher: ShiftCipher

    val alphabetSet = setOf(
        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
        "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
        "u", "v", "w", "x", "y", "z"
    )

    val morseSet = setOf(
        ".-", "-...", "-.-.", "-..", ".",
        "..-.", "--.", "....", "..", ".---",
        "-.-", ".-..", "--", "-.", "--.--",
        "---", ".--.", "--.-", ".-.", "...",
        "-", "..-", "...-", ".--", "-..-",
        "-.--", "--..",
        "-----", ".----", "..---", "...--",
        "....-", ".....", "-....", "--...",
        "---..", "----.",
        ".-.-.-", "-.-.--", "..--..", ".-..-."
    )


    @BeforeEach
    fun onBefore()
    {
        alphabetCipher = ShiftCipher(
            alphabetSet,
            wordSeparator = " ",
            symbolSeparator = "",
            cipherKey = "abc"
        )

        morseCipher = ShiftCipher(
            morseSet,
            wordSeparator = "/",
            symbolSeparator = " ",
            cipherKey = "-. -... -.-."
        )
    }

    @Test
    fun `Alphabet - The decoded string of an encoded string must be equal to the original string`()
    {
        val initialString = "the quick foxy jumps over the lazy dog"

        val encodedString = alphabetCipher.encode(initialString)
        val decodedString = alphabetCipher.decode(encodedString)

        assertThat(decodedString).isEqualTo(initialString)
    }

    @Test
    fun `Morse - The decoded string of an encoded string must be equal to the original string`()
    {
        val initialString = "- .... . / --.- ..- .. -.-. -.- / ..-. --- -..- -.-- / .--- ..- -- .--. ... / --- ...- . .-. / - .... . / .-.. .- --.. -.-- / -.. --- --."

        val encodedString = morseCipher.encode(initialString)
        val decodedString = morseCipher.decode(encodedString)

        assertThat(decodedString).isEqualTo(initialString)
    }
//
//    @Test
//    fun `Alphabet - Check encoding is correct`()
//    {
//        val initialString = "abcdefghijklmnñopqrstuvwxyz 0123456789"
//
//        val encodedString = cipher.encode(initialString)
//        val morseString = ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --.-- --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --.. / ----- .---- ..--- ...-- ....- ..... -.... --... ---.. ----."
//
//        assertThat(encodedString).isEqualTo(morseString)
//    }
//
//    @Test
//    fun `Morse - Check decoding is correct`()
//    {
//        val initialString = ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --.-- --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --.. / ----- .---- ..--- ...-- ....- ..... -.... --... ---.. ----."
//
//        val decodedString = cipher.decode(initialString)
//        val naturalString = "abcdefghijklmnñopqrstuvwxyz 0123456789"
//
//        assertThat(decodedString).isEqualTo(naturalString)
//    }
}