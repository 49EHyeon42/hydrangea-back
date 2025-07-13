package dev.ehyeon.hydrangea

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HydrangeaApplication

fun main(args: Array<String>) {
    runApplication<HydrangeaApplication>(*args)
}
