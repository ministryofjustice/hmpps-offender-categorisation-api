package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.file

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.DeleteObjectRequest
import aws.sdk.kotlin.services.s3.model.GetObjectResponse
import aws.sdk.kotlin.services.s3.model.ListObjectsV2Response
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import software.amazon.awssdk.utils.StringInputStream
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.file.FileType
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.file.PendingFile
import java.io.File
import java.io.InputStream
import java.sql.Timestamp
import java.time.Duration
import java.time.LocalDateTime
import aws.sdk.kotlin.services.s3.model.Object as S3Object

@RunWith(MockitoJUnitRunner::class)
class S3FileServiceTest {
  @Test
  internal fun `process viper file`() {
    val pendingFile = runBlocking {
      withTimeout(Duration.ofMinutes(5).toMillis()) {
        loadViperFile()
      }
    }
    val viperFile = loadTestData("VIPER_2_2024_10_29.csv")
    assertThat(pendingFile?.data!!).isEqualTo(viperFile)
  }

  private suspend fun loadViperFile(): PendingFile? {
    val s3Client = mockS3Client("viper/VIPER_2_file.csv", loadTestData("VIPER_2_2024_10_29.csv"))
    val service = S3FileService(s3Client)

    return service.getLatestViper2File("risk-profiler/viper/VIPER_2_file.csv", FileType.VIPER)
  }

  private fun loadTestData(filename: String): String {
    val inputStream: InputStream = File("src/test/resources/testdata/$filename").inputStream()
    return inputStream.bufferedReader().use { it.readText() }
  }

  @Test
  internal fun `delete historical files`() {
    runBlocking {
      withTimeout(Duration.ofMinutes(5).toMillis()) {
        testDeleteHistoricalFiles()
      }
    }
  }

  private suspend fun testDeleteHistoricalFiles() {
    val mockFile1 = Mockito.mock(S3Object::class.java)
    Mockito.`when`(mockFile1.key).thenReturn("s3Ob1")
    Mockito.`when`(mockFile1.lastModified).thenReturn(
      aws.smithy.kotlin.runtime.time
        .Instant(Timestamp.valueOf(LocalDateTime.of(2019, 10, 1, 1, 1)).toInstant()),
    )

    val mockFile2 = Mockito.mock(S3Object::class.java)
    Mockito.`when`(mockFile2.key).thenReturn("s3Ob2")
    Mockito.`when`(mockFile2.lastModified).thenReturn(
      aws.smithy.kotlin.runtime.time
        .Instant(Timestamp.valueOf(LocalDateTime.of(2019, 10, 2, 2, 2)).toInstant()),
    )

    val mockFile3 = Mockito.mock(S3Object::class.java)
    Mockito.`when`(mockFile3.key).thenReturn("s3Ob3")
    Mockito.`when`(mockFile3.lastModified).thenReturn(
      aws.smithy.kotlin.runtime.time
        .Instant(Timestamp.valueOf(LocalDateTime.of(2019, 10, 3, 3, 3)).toInstant()),
    )

    val mockFile4 = Mockito.mock(S3Object::class.java)
    Mockito.`when`(mockFile4.key).thenReturn("s3Ob4")
    Mockito.`when`(mockFile4.lastModified).thenReturn(
      aws.smithy.kotlin.runtime.time
        .Instant(Timestamp.valueOf(LocalDateTime.of(2019, 10, 4, 4, 4)).toInstant()),
    )

    val s3ObjectSummary = listOf(
      mockFile1,
      mockFile2,
      mockFile3,
      mockFile4,
    )

    val s3Client = mockS3Client("viper/VIPER_2_file.csv", loadTestData("VIPER_2_2024_10_29.csv"))

    val result = Mockito.mock(ListObjectsV2Response::class.java)
    val service = S3FileService(s3Client)

    Mockito.`when`(s3Client.listObjectsV2(any())).thenReturn(result)
    Mockito.`when`(result.contents)
      .thenReturn(s3ObjectSummary as List<S3Object>)
    service.deleteHistoricalFiles("risk-profiler/gg")
    Mockito.verify(s3Client).deleteObject(
      DeleteObjectRequest {
        key = "s3Ob1"
        bucket = "risk-profiler"
      },
    )
    Mockito.verify(s3Client).deleteObject(
      DeleteObjectRequest {
        key = "s3Ob2"
        bucket = "risk-profiler"
      },
    )
    Mockito.verify(s3Client, Mockito.never()).deleteObject(
      DeleteObjectRequest {
        key = "s3Ob3"
        bucket = "risk-profiler"
      },
    )
    Mockito.verify(s3Client, Mockito.never()).deleteObject(
      DeleteObjectRequest {
        key = "s3Ob4"
        bucket = "risk-profiler"
      },
    )
  }

  private suspend fun mockS3Client(key: String, fileToProcess: String): S3Client {
    val s3Client = Mockito.mock(S3Client::class.java)
    val listObjectsV2Result = Mockito.mock(ListObjectsV2Response::class.java)

    val mockS3Object = Mockito.mock(S3Object::class.java)
    Mockito.`when`(mockS3Object.key).thenReturn(key)

    val s3ObjectSummary = listOf<S3Object>(
      mockS3Object,
    )

    Mockito.`when`(listObjectsV2Result.contents).thenReturn(s3ObjectSummary)
    Mockito.`when`(s3Client.listObjectsV2(any()))
      .thenReturn(listObjectsV2Result)

    val testInputStream = StringInputStream(fileToProcess).string
    Mockito.`when`(
      s3Client.getObject(
        any(),
        any<suspend (GetObjectResponse) -> String>(),
      ),
    )
      .thenReturn(testInputStream)
    return s3Client
  }
}
