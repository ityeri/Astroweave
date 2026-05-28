package com.github.ityeri.astroweave.util

import io.mola.galimatias.GalimatiasParseException
import io.mola.galimatias.URL

fun String.isValidUrl(baseUrl: URL? = null): Boolean =
    if (this.isBlank()) false
    else if (
        this.startsWith("/")
        || this.startsWith("./")
        || this.startsWith("../")
        ) {
        if (this.contains(" ")) false

        if (baseUrl == null) true
        else
            try {
                URL.parse(baseUrl, this)
                true
            } catch (_: GalimatiasParseException) {
                false
            }

    } else {
        try {
            val url = URL.parse(this)

            val scheme = url.scheme()
            if (scheme != "http" && scheme != "https") return false

            val host = url.host()
            if (host == null || host.toString().isBlank()) return false

            true

        } catch (e: GalimatiasParseException) {
            false
        }
    }
