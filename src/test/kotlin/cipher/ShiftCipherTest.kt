package cipher

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class ShiftCipherTest
{
    private lateinit var cipher: ShiftCipher

    private val morseSet = setOf(
        ".-", "-...", "-.-.", "-..", ".",
        "..-.", "--.", "....", "..", ".---",
        "-.-", ".-..", "--", "-.", "--.--",
        "---", ".--.", "--.-", ".-.", "...",
        "-", "..-", "...-", ".--", "-..-",
        "-.--", "--.."
    )

    private val morseString = ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --.-- --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --.."
    private val shiftedMorseString = "-... -.. ..-. . --. .. .... .--- .-.. -.- -- --.-- -. --- --.- .--. .-. - ... ..- .-- ...- -..- --.. -.-- .- -.-."

    @BeforeEach
    fun onBefore()
    {
        cipher = ShiftCipher(
            morseSet,
            wordSeparator = "/",
            symbolSeparator = " ",
            cipherKey = ".- -... -.-."
        )
    }

    @Test
    fun `Decode an encoded string must be equal to the original one`()
    {
        val encodedString = cipher.encode(morseString)
        val decodedString = cipher.decode(encodedString)

        assertThat(decodedString).isEqualTo(morseString)
    }

    @Test
    fun `Encode is correct`()
    {
        val encodedString = cipher.encode(morseString)

        assertThat(encodedString).isEqualTo(shiftedMorseString)
    }

    @Test
    fun `Decode is correct`()
    {
        val decodedString = cipher.decode(shiftedMorseString)

        assertThat(decodedString).isEqualTo(morseString)
    }
}
