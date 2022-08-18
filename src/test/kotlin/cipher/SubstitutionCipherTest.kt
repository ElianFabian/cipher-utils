package cipher

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class SubstitutionCipherTest
{
    private lateinit var cipher: SubstitutionCipher

    private val toMorseMap = mapOf(
        "a" to ".-", "b" to "-...", "c" to "-.-.", "d" to "-..", "e" to ".",
        "f" to "..-.", "g" to "--.", "h" to "....", "i" to "..", "j" to ".---",
        "k" to "-.-", "l" to ".-..", "m" to "--", "n" to "-.", "ñ" to "--.--",
        "o" to "---", "p" to ".--.", "q" to "--.-", "r" to ".-.", "s" to "...",
        "t" to "-", "u" to "..-", "v" to "...-", "w" to ".--", "x" to "-..-",
        "y" to "-.--", "z" to "--..",
        "0" to "-----", "1" to ".----", "2" to "..---", "3" to "...--",
        "4" to "....-", "5" to ".....", "6" to "-....", "7" to "--...",
        "8" to "---..", "9" to "----.",
        "." to ".-.-.-", "," to "-.-.--", "?" to "..--..", "\"" to ".-..-."
    )


    @BeforeEach
    fun onBefore()
    {
        cipher = SubstitutionCipher(
            encodeMap = toMorseMap,
            decodedWordSeparator = " ",
            decodedSymbolSeparator = "",
            encodedWordSeparator = "/",
            encodedSymbolSeparator = " "
        )
    }

    @Test
    fun `Decode an encoded string must be equal to the original one`()
    {
        val initialString = "supercalifragilisticexpialidocious and 299792485"

        val encodedString = cipher.encode(initialString)
        val decodedString = cipher.decode(encodedString)

        assertThat(decodedString).isEqualTo(initialString)
    }

    @Test
    fun `Encode is correct`()
    {
        val initialString = "abcdefghijklmnñopqrstuvwxyz 0123456789"

        val encodedString = cipher.encode(initialString)
        val expectedEncodedString = ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --.-- --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --.. / ----- .---- ..--- ...-- ....- ..... -.... --... ---.. ----."

        assertThat(encodedString).isEqualTo(expectedEncodedString)
    }

    @Test
    fun `Decode is correct`()
    {
        val initialString = ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --.-- --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --.. / ----- .---- ..--- ...-- ....- ..... -.... --... ---.. ----."

        val decodedString = cipher.decode(initialString)
        val expectedDecodedString = "abcdefghijklmnñopqrstuvwxyz 0123456789"

        assertThat(decodedString).isEqualTo(expectedDecodedString)
    }
}