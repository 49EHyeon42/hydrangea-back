package dev.ehyeon.hydrangea

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan("dev.ehyeon.hydrangea.common.property")
class HydrangeaApplication

fun main(args: Array<String>) {
    runApplication<HydrangeaApplication>(*args)
}
