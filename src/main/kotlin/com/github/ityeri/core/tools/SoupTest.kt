package com.github.ityeri.core.tools

import com.github.ityeri.utils.getLinks
import org.jsoup.Jsoup

fun main() {
    val url = "https://namu.wiki"

    val doc = Jsoup.connect(url).get()

    doc.getLinks().forEach {
        println(it)
    }

}
