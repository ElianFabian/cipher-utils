package util

/**
 * The regular String.split() with an empty string adds an
 * empty string at the beginning and ending of the result list.
 * And this function avoids that.
 */
fun String.trimmedSplit(separator: String): List<String> {
	if (separator == "") {
		return this.trim().toCharArray().map { it.toString() }
	}
	return this.trim().split(separator)
}
