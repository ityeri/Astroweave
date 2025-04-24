package com.github.ityeri.utils

import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.net.*

fun encodeUrlComponent(segment: String): String {
    return URLEncoder.encode(segment, StandardCharsets.UTF_8.toString())
        .replace("+", "%20")
}

fun String.normalizeUrl(): String {
    // 스킴 없으면 추가
    val ensuredUrl = if (!this.startsWith("https")) "https://$this" else this

    val url = URL(ensuredUrl)

    val scheme = url.protocol
    val host = url.host.removePrefix("www.")
    val rawPath = url.path

    // 퍼센트 인코딩 적용
    val encodedPath = rawPath.split("/").joinToString("/") { segment ->
        if (segment.isEmpty()) "" else encodeUrlComponent(segment)
    }

    val normalized = "$scheme://$host$encodedPath"

    return normalized.trimEnd('/')
}


