package com.interminable.subscribers.manager

import com.interminable.subscribers.manager.storage.IgnitePersistence
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class App

fun main(args: Array<String>) {
    IgnitePersistence().start()
    runApplication<App>(*args)
}
