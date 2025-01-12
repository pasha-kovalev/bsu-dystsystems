package by.bsu.image.processor.controller

import aws.sdk.kotlin.services.s3.model.NoSuchKey
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import by.bsu.image.processor.service.ImageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

@RestController
@RequestMapping("/images")
class ImageController(private val imageService: ImageService) {

    @Operation(
        summary = "Получение изображения",
        description = "Возвращает изображение по его идентификатору"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Изображение найдено",
        content = [Content(mediaType = MediaType.IMAGE_JPEG_VALUE)]
    )
    @GetMapping("/{imageId}", produces = [MediaType.IMAGE_JPEG_VALUE])
    suspend fun getImage(@PathVariable imageId: String): ResponseEntity<ByteArray> {
        var imageData: ByteArray? = null;
        try {
            imageData = imageService.getImage(imageId)
        } catch (_: NoSuchKey) {
            return ResponseEntity.notFound().build<ByteArray>()
        }

        return ResponseEntity.ok()
            .header(CONTENT_TYPE, "image/jpeg")
            .body(imageData)
    }

    @Operation(
        summary = "Загрузка изображения",
        description = "Загружает изображение и возвращает его идентификатор"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Изображение успешно загружено",
        content = [Content(mediaType = "application/json")]
    )
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun uploadImage(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        val imageId = imageService.uploadImage(file)
        return ResponseEntity.ok(imageId)
    }

    @Operation(
        summary = "Обновление изображения",
        description = "Обновляет изображение по указанному id"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Изображение успешно загружено",
        content = [Content(mediaType = "application/json")]
    )
    @PutMapping("/{id}")
    suspend fun updateImage(
        @PathVariable id: String,
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<HttpStatus> {
        try {
            imageService.uploadImage(id, file, true)
        } catch (_: NoSuchKey) {
            return ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND)
        }

        imageService.uploadImage(id, file, true)
        return ResponseEntity<HttpStatus>(HttpStatus.OK)
    }

    @Operation(
        summary = "Удаление изображения",
        description = "Удаляет изображение по его идентификатору"
    )
    @ApiResponse(
        responseCode = "204",
        description = "Изображение успешно удалено"
    )
    @DeleteMapping("/{imageId}")
    suspend fun deleteImage(@PathVariable imageId: String): ResponseEntity<HttpStatus> {
        imageService.deleteImage(imageId)
        return ResponseEntity<HttpStatus>(HttpStatus.OK)
    }
}
