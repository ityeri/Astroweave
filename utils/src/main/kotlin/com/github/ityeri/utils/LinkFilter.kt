package com.github.ityeri.utils

import org.jsoup.nodes.Document

fun Document.getLinks(): List<String> {
    return select("a[href]").map {
        it.attr("abs:href")
    }
}
