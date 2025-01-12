package by.bsu.image.processor.service

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.DeleteObjectRequest
import aws.sdk.kotlin.services.s3.model.DeleteObjectRequest.Companion.invoke
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.model.GetObjectRequest.Companion.invoke
import aws.sdk.kotlin.services.s3.model.PutObjectRequest.Companion.invoke
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.content.toByteArray
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

private val logger = KotlinLogging.logger {}

@Service
class ImageService(
    private val s3Client: S3Client,
    @Value("\${yandex.storage.bucket-name}")
    private val bucketName: String
) {
    suspend fun uploadImage(file: MultipartFile): String = withContext(Dispatchers.IO) {
        val imageId = UUID.randomUUID().toString()
        return@withContext uploadImage(imageId, file)
    }

    suspend fun uploadImage(
        imageId: String,
        file: MultipartFile,
        isUpdate: Boolean = false
    ): String =
        withContext(Dispatchers.IO) {
            try {
                if (isUpdate) {
                    getImage(imageId)
                }
                s3Client.putObject(PutObjectRequest {
                    bucket = bucketName
                    key = "images/$imageId"
                    body = ByteStream.fromBytes(file.bytes)
                    contentType = file.contentType
                })
                logger.info { "Successfully uploaded image with ID: $imageId" }
                return@withContext imageId
            } catch (e: Exception) {
                logger.error(e) { "Failed to upload image" }
                throw RuntimeException("Failed to upload image", e)
            }
        }

    suspend fun getImage(imageId: String): ByteArray = withContext(Dispatchers.IO) {
        val request =
            GetObjectRequest {
                key = "images/$imageId"
                bucket = bucketName
            }

        val response = s3Client.getObject(request) { resp ->
            resp.body?.toByteArray()
        }
        logger.info { "Successfully retrieved image with ID: $imageId" }
        return@withContext response
            ?: throw RuntimeException("Empty response body")
    }

    suspend fun deleteImage(imageId: String) = withContext(Dispatchers.IO) {
        try {
            val request =
                DeleteObjectRequest {
                    bucket = bucketName
                    key = "images/$imageId"
                }

            s3Client.deleteObject(request)
            logger.info { "Successfully deleted image with ID: $imageId" }
        } catch (e: Exception) {
            logger.error(e) { "Failed to delete image with ID: $imageId" }
            throw RuntimeException("Failed to delete image", e)
        }
    }
}