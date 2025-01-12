package by.bsu.image.processor.config

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider.Companion.invoke
import aws.sdk.kotlin.services.s3.S3Client
import aws.smithy.kotlin.runtime.net.url.Url
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Value

@Configuration
class YandexStorageConfig {
    @Value("\${yandex.storage.access-key}")
    lateinit var accessKey: String

    @Value("\${yandex.storage.secret-key}")
    lateinit var secretKey: String

    @Value("\${yandex.storage.region}")
    lateinit var region: String

    @Bean
    fun s3Client(): S3Client {

        return S3Client {
            region = this@YandexStorageConfig.region
            endpointUrl =  Url.parse("https://storage.yandexcloud.net")
            credentialsProvider = StaticCredentialsProvider {
                accessKeyId = accessKey
                secretAccessKey = secretKey
            }
        }
    }
}
