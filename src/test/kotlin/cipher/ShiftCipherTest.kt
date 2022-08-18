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
        "-.--", "--..",
        "-----", ".----", "..---", "...--",
        "....-", ".....", "-....", "--...",
        "---..", "----.",
        ".-.-.-", "-.-.--", "..--..", ".-..-."
    )


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
        val initialString = "- .... . / --.- ..- .. -.-. -.- / ..-. --- -..- -.-- / .--- ..- -- .--. ... / --- ...- . .-. / - .... . / .-.. .- --.. -.-- / -.. --- --."

        val encodedString = cipher.encode(initialString)
        val decodedString = cipher.decode(encodedString)

        assertThat(decodedString).isEqualTo(initialString)
    }

    @Test
    fun `Encode is correct`()
    {
        val initialString = ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --.-- --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --.. / ----- .---- ..--- ...-- ....- ..... -.... --... ---.. ----."

        val encodedString = cipher.encode(initialString)
        val expectedEncodedString = "-... -.. ..-. . --. .. .... .--- .-.. -.- -- --.-- -. --- --.- .--. .-. - ... ..- .-- ...- -..- --.. -.-- ----- ..--- / .---- ...-- ..... ....- -.... ---.. --... ----. -.-.-- .-.-.-"

        assertThat(encodedString).isEqualTo(expectedEncodedString)
    }

    @Test
    fun `Decode is correct`()
    {
        val initialString = "-... -.. ..-. . --. .. .... .--- .-.. -.- -- --.-- -. --- --.- .--. .-. - ... ..- .-- ...- -..- --.. -.-- ----- ..--- / .---- ...-- ..... ....- -.... ---.. --... ----. -.-.-- .-.-.-"

        val decodedString = cipher.decode(initialString)
        val expectedDecodedString = ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --.-- --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --.. / ----- .---- ..--- ...-- ....- ..... -.... --... ---.. ----."

        assertThat(decodedString).isEqualTo(expectedDecodedString)
    }
}