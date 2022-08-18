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

    private val naturalString = "abcdefghijklmnñopqrstuvwxyz and oxofempal"
    private val morseString = ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --.-- --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --.. / .- -. -.. / --- -..- --- ..-. . -- .--. .- .-.."
    private val gboard1String = "@;'€3_&-8+()?!/9014#57:2\"6* @!€ 9\"9_3?0@)"


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
        val encodedString = morseCipher.encode(naturalString)
        val decodedString = morseCipher.decode(encodedString)

        assertThat(decodedString).isEqualTo(naturalString)
    }

    @Test
    fun `Morse - Encode is correct`()
    {
        val encodedString = morseCipher.encode(naturalString)

        assertThat(encodedString).isEqualTo(morseString)
    }

    @Test
    fun `Morse - Decode is correct`()
    {
        val decodedString = morseCipher.decode(morseString)

        assertThat(decodedString).isEqualTo(naturalString)
    }

    @Test
    fun `GBoard1 - Decode an encoded string must be equal to the original one`()
    {
        val encodedString = gboard1Cipher.encode(naturalString)
        val decodedString = gboard1Cipher.decode(encodedString)

        assertThat(decodedString).isEqualTo(naturalString)
    }

    @Test
    fun `GBoard1 - Encode is correct`()
    {
        val encodedString = gboard1Cipher.encode(naturalString)

        assertThat(encodedString).isEqualTo(gboard1String)
    }

    @Test
    fun `GBoard1 - Decode is correct`()
    {
        val decodedString = gboard1Cipher.decode(gboard1String)

        assertThat(decodedString).isEqualTo(naturalString)
    }
}
