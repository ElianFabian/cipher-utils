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
        "y" to "-.--", "z" to "--.."
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
        val initialString = "supercalifragilisticexpialidocious and oxofempal"

        val encodedString = cipher.encode(initialString)
        val decodedString = cipher.decode(encodedString)

        assertThat(decodedString).isEqualTo(initialString)
    }

    @Test
    fun `Encode is correct`()
    {
        val initialString = "abcdefghijklmnñopqrstuvwxyz and oxofempal"

        val encodedString = cipher.encode(initialString)
        val expectedEncodedString = ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --.-- --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --.. / .- -. -.. / --- -..- --- ..-. . -- .--. .- .-.."

        assertThat(encodedString).isEqualTo(expectedEncodedString)
    }

    @Test
    fun `Decode is correct`()
    {
        val initialString = ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --.-- --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --.. / .- -. -.. / --- -..- --- ..-. . -- .--. .- .-.."

        val decodedString = cipher.decode(initialString)
        val expectedDecodedString = "abcdefghijklmnñopqrstuvwxyz and oxofempal"

        assertThat(decodedString).isEqualTo(expectedDecodedString)
    }
}