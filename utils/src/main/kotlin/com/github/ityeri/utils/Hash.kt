package com.github.ityeri.utils

import java.security.MessageDigest

fun String.toSha256(): String {
    val bytes = MessageDigest.getInstance("SHA-256")
        .digest(toByteArray(Charsets.UTF_8))

    return bytes.joinToString("") { "%02x".format(it) }  // 16진수 문자열로 변환
}
