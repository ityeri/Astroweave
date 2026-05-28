package com.github.ityeri.astroweave.util

import org.jsoup.nodes.Document
import kotlin.collections.flatMap

fun Document.extractAllAttrs(): List<String> =
    body().select("*").flatMap { element ->
        element.attributes().flatMap { attribute ->
            if (attribute.key.lowercase() == "srcset") {
                attribute.value.split(',').map { s -> s.trim() }
            } else {
                listOf(attribute.value)
            }
        }
    }
