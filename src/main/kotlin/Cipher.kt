interface Cipher {
	fun encode(decodedText: String): String
	fun decode(encodedText: String): String
}
