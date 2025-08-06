package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.file

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.listObjectsV2
import aws.sdk.kotlin.services.s3.model.DeleteObjectRequest
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.smithy.kotlin.runtime.content.decodeToString
import aws.smithy.kotlin.runtime.time.toJvmInstant
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.file.FileInfo
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.file.FileType
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.file.PendingFile
import java.io.IOException
import java.time.ZoneId

@Service
class S3FileService(
  private val s3Client: S3Client,
) {

  suspend fun getLatestViper2File(fileLocation: String, fileType: FileType?): PendingFile? {
    val bucketAndPrefix = BucketAndPrefix(fileLocation)
    val s3Result = list(bucketAndPrefix)
    log.info("Found {} objects in {}", s3Result?.count(), fileLocation)

    val latestViperFile = s3Result
      ?.filter { o -> o.key?.contains("VIPER_2_") == true }
      ?.sortedBy { o -> o.lastModified }
      ?.first()

    if (latestViperFile == null) {
      return null
    }

    try {
      val request = GetObjectRequest {
        bucket = bucketAndPrefix.bucketName
        key = latestViperFile.key
      }
      val response = s3Client.getObject(request) { it.body?.decodeToString() ?: "" }
      return PendingFile(
        latestViperFile.key,
        latestViperFile.lastModified
          ?.atZone(ZoneId.systemDefault())
          ?.toLocalDateTime(),
        response,
      )
    } catch (e: IOException) {
      log.error(e.message)
    }
    return null
  }

  suspend fun deleteHistoricalFiles(fileLocation: String) {
    val bucketAndPrefix = BucketAndPrefix(fileLocation)
    val s3ObjectResult = list(bucketAndPrefix)
    log.info(
      "Found {} data files for data housekeeping in {}",
      s3ObjectResult?.count(),
      fileLocation,
    )
    s3ObjectResult?.sortedBy { o -> o.lastModified }?.reversed()?.asIterable()?.drop(2)?.forEach { o ->
      deleteFile(bucketAndPrefix, o.key!!)
    }
  }

  private suspend fun deleteFile(bucketAndPrefix: BucketAndPrefix, objectKey: String) {
    try {
      val deleteObjectRequest = DeleteObjectRequest {
        bucket = bucketAndPrefix.bucketName
        key = objectKey
      }
      s3Client.deleteObject(deleteObjectRequest)
    } catch (e: IOException) {
      log.error(e.message)
    }
    log.info("Deleted s3 data file: {} from bucket {}", objectKey, bucketAndPrefix.bucketName)
  }

  private suspend fun list(bucketAndPrefix: BucketAndPrefix): List<FileInfo>? = s3Client.listObjectsV2 {
    bucket = bucketAndPrefix.bucketName
    prefix = bucketAndPrefix.prefix
  }.contents
    ?.filter { StringUtils.isNotEmpty(it.key) }
    ?.map { FileInfo(key = it.key, lastModified = it.lastModified?.toJvmInstant(), size = it.size) }

  private data class BucketAndPrefix(
    var bucketName: String?,
    var prefix: String?,
  ) {

    constructor(fileLocation: String) : this(null, null) {

      val split = StringUtils.split(fileLocation, "/")
      bucketName = split[0]
      prefix = if (split.size > 1) split[1] else null
    }
  }

  companion object {
    private val log = LoggerFactory.getLogger(S3FileService::class.java)
  }
}
