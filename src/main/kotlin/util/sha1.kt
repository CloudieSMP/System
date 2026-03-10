package util

import java.security.MessageDigest

fun sha1(data: ByteArray): String {
    val algorithm = "SHA-1"
    return calculateDigestForAlgorithm(algorithm, data)
}

@Suppress("SameParameterValue")
private fun calculateDigestForAlgorithm(algorithm: String, data: ByteArray): String {
    val digest = MessageDigest.getInstance(algorithm)
    val hashedBytes = digest.digest(data)
    return hashedBytes.joinToString("") { "%02x".format(it) }
}