package cipher

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class SubstitutionCipherTest
{
    private lateinit var morseCipher: SubstitutionCipher
    private lateinit var gboard1Cipher: SubstitutionCipher

    private val toMorseMap = mapOf(
        "a" to ".-", "b" to "-...", "c" to "-.-.", "d" to "-..", "e" to ".",
        "f" to "..-.", "g" to "--.", "h" to "....", "i" to "..", "j" to ".---",
        "k" to "-.-", "l" to ".-..", "m" to "--", "n" to "-.", "ñ" to "--.--",
        "o" to "---", "p" to ".--.", "q" to "--.-", "r" to ".-.", "s" to "...",
        "t" to "-", "u" to "..-", "v" to "...-", "w" to ".--", "x" to "-..-",
        "y" to "-.--", "z" to "--.."
    )

    private val toGBoard1 = mapOf(
        "a" to "@", "b" to ";", "c" to "'", "d" to "€", "e" to "3",
        "f" to "_", "g" to "&", "h" to "-", "i" to "8", "j" to "+",
        "k" to "(", "l" to ")", "m" to "?", "n" to "!", "ñ" to "/",
        "o" to "9", "p" to "0", "q" to "1", "r" to "4", "s" to "#",
        "t" to "5", "u" to "7", "v" to ":", "w" to "2", "x" to "\"",
        "y" to "6", "z" to "*",
    )


    @BeforeEach
    fun onBefore()
    {
        morseCipher = SubstitutionCipher(
            encodeMap = toMorseMap,
            decodedWordSeparator = " ",
            decodedSymbolSeparator = "",
            encodedWordSeparator = "/",
            encodedSymbolSeparator = " "
        )

        gboard1Cipher = SubstitutionCipher(
            toGBoard1,
            decodedWordSeparator = " ",
            decodedSymbolSeparator = "",
            encodedWordSeparator = " ",
            encodedSymbolSeparator = ""
        )
    }

    @Test
    fun `Morse - Decode an encoded string must be equal to the original one`()
    {
        val initialString = "supercalifragilisticexpialidocious and oxofempal"

        val encodedString = morseCipher.encode(initialString)
        val decodedString = morseCipher.decode(encodedString)

        assertThat(decodedString).isEqualTo(initialString)
    }

    @Test
    fun `Morse - Encode is correct`()
    {
        val initialString = "abcdefghijklmnñopqrstuvwxyz and oxofempal"

        val encodedString = morseCipher.encode(initialString)
        val expectedEncodedString = ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --.-- --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --.. / .- -. -.. / --- -..- --- ..-. . -- .--. .- .-.."

        assertThat(encodedString).isEqualTo(expectedEncodedString)
    }

    @Test
    fun `Morse - Decode is correct`()
    {
        val initialString = ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --.-- --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --.. / .- -. -.. / --- -..- --- ..-. . -- .--. .- .-.."

        val decodedString = morseCipher.decode(initialString)
        val expectedDecodedString = "abcdefghijklmnñopqrstuvwxyz and oxofempal"

        assertThat(decodedString).isEqualTo(expectedDecodedString)
    }

    @Test
    fun `GBoard1 - Decode an encoded string must be equal to the original one`()
    {
        val initialString = "supercalifragilisticexpialidocious and oxofempal"

        val encodedString = gboard1Cipher.encode(initialString)
        val decodedString = gboard1Cipher.decode(encodedString)

        assertThat(decodedString).isEqualTo(initialString)
    }

    @Test
    fun `GBoard1 - Encode is correct`()
    {
        val initialString = "abcdefghijklmnñopqrstuvwxyz and oxofempal"

        val encodedString = gboard1Cipher.encode(initialString)
        val expectedEncodedString = "@;'€3_&-8+()?!/9014#57:2\"6* @!€ 9\"9_3?0@)"

        assertThat(encodedString).isEqualTo(expectedEncodedString)
    }

    @Test
    fun `GBoard1 - Decode is correct`()
    {
        val initialString = "@;'€3_&-8+()?!/9014#57:2\"6* @!€ 9\"9_3?0@)"

        val decodedString = gboard1Cipher.decode(initialString)
        val expectedDecodedString = "abcdefghijklmnñopqrstuvwxyz and oxofempal"

        assertThat(decodedString).isEqualTo(expectedDecodedString)
    }
}
