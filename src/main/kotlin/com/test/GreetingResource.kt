package com.test

import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import org.eclipse.microprofile.config.inject.ConfigProperty
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.checksums.RequestChecksumCalculation
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.net.URI

@Path("/")
class GreetingResource {
    @Inject
    lateinit var s3ClientFromExtension: S3Client

    @ConfigProperty(name = "ceph.ssec.key.base64")
    lateinit var currentSSEKey: String

    @ConfigProperty(name = "ceph.ssec.key.md5.base64")
    lateinit var currentSSEKeyMd5: String

    @ConfigProperty(name = "ceph.ssec.algo")
    lateinit var currentSSEAlgo: String

    @ConfigProperty(name = "quarkus.s3.aws.credentials.static-provider.access-key-id")
    lateinit var accessKey: String

    @ConfigProperty(name = "quarkus.s3.aws.credentials.static-provider.secret-access-key")
    lateinit var secretKey: String

    @ConfigProperty(name = "quarkus.s3.endpoint-override")
    lateinit var endpoint: String

    @ConfigProperty(name = "ceph.bucket.name")
    lateinit var bucketName: String

    val s3ClientManual: S3Client by lazy {
        val credentials = AwsBasicCredentials.create(accessKey, secretKey)
        S3Client.builder()
            .endpointOverride(URI(endpoint))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .serviceConfiguration {
                it.pathStyleAccessEnabled();
            }
            // this is where the magic happens
            .requestChecksumCalculation(RequestChecksumCalculation.WHEN_REQUIRED)
            .region(Region.US_EAST_1)
            .build()
    }

    @GET
    @Path("/writeWithExtensionClient")
    fun writeWithExtensionClient()  {
        val request = PutObjectRequest.builder()
            .sseCustomerKey(currentSSEKey)
            .sseCustomerAlgorithm(currentSSEAlgo)
            .sseCustomerKeyMD5(currentSSEKeyMd5)
            .bucket(bucketName)
            .key("/some/path/extension")
            .build()
        s3ClientFromExtension.putObject(request, RequestBody.fromBytes("body".toByteArray(Charsets.UTF_8)))
    }

    @GET
    @Path("/writeWithManualClient")
    fun writeWithManualClient()  {
        val request = PutObjectRequest.builder()
            .sseCustomerKey(currentSSEKey)
            .sseCustomerAlgorithm(currentSSEAlgo)
            .sseCustomerKeyMD5(currentSSEKeyMd5)
            .bucket(bucketName)
            .key("/some/path/extension")
            .build()
        s3ClientManual.putObject(request, RequestBody.fromBytes("body".toByteArray(Charsets.UTF_8)))
    }

}
