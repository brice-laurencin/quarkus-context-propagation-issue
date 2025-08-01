package care.resilience.billing

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.CephContainer

private const val ACCESS_KEY = "owncloud123456"

private const val SECRET_KEY = "secret123456"

private const val CEPH_PORT = 8080

class S3TestResource : QuarkusTestResourceLifecycleManager {

    val bucketName = "mybucket"

    private val cephContainer: CephContainer = run {
        val os = System.getProperty("os.name").lowercase()
        when {
            os.contains("mac") ->
                CephContainer("quay.io/ceph/demo:main-82d8690-quincy-centos-arm64-stream8-aarch64")

            else -> CephContainer()
        }
    }

    private val ceph = cephContainer.withCephBucket(bucketName)
        .withSslDisabled()
        .withCephAccessKey(ACCESS_KEY)
        .withCephSecretKey(SECRET_KEY)

    override fun start(): Map<String?, String?> {
        ceph.start()
        Thread.sleep(500) // to wait for ceph to be really started
        val s3URL = ceph.cephUrl.toString()

        return mapOf(
            "quarkus.s3.endpoint-override" to s3URL,
            "quarkus.s3.aws.credentials.static-provider.access-key-id" to ACCESS_KEY,
            "quarkus.s3.aws.credentials.static-provider.secret-access-key" to SECRET_KEY,
            "quarkus.s3.path-style-access" to "true",
            // key: echo -n this_is_my_key | base64 -w0
            // key md5: echo -n this_is_my_key | openssl dgst -md5 -binary | base64 -w0
            "ceph.ssec.key.base64" to "MjdxbW1qOWg2M3ZxM3R6MHJ3N21paXlzeDlnNXM2NjY=",
            "ceph.ssec.key.md5.base64" to "oB0FbxCCdFpVeTfoiXCfOg==",
            "ceph.ssec.algo" to "AES256",
            "ceph.bucket.name" to bucketName,
        )
    }

    override fun stop() {
        ceph.stop()
    }
}
