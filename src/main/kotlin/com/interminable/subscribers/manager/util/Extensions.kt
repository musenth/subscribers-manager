package com.interminable.subscribers.manager.util

fun String.replaceLineBreaks() = this.replace("""\n\s*[|]?""".toRegex(), " ")