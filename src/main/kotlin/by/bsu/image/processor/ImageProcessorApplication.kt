package by.bsu.image.processor

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@OpenAPIDefinition(info = Info(title = "Image REST API", version = "1.0"))
class ImageProcessorApplication

fun main(args: Array<String>) {
    runApplication<ImageProcessorApplication>(*args)
}