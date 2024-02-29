package com.example.marsphotos.util

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSS
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import java.io.File
import java.io.FileOutputStream

fun createFileFromContentUri(context: Context, contentUri: Uri): File? {
    val contentResolver = context.contentResolver
    val fileType = contentResolver.getType(contentUri)
    val fileExtension = fileType?.substringAfterLast('/')

    // 创建临时文件，前缀可以根据是图片还是视频来调整
    val prefix = when {
        fileType?.startsWith("image") == true -> "image_"
        fileType?.startsWith("video") == true -> "video_"
        else -> "upload_"
    }

    val tempFile = File.createTempFile(prefix, ".$fileExtension", context.cacheDir)
    tempFile.deleteOnExit()

    contentResolver.openInputStream(contentUri)?.use { inputStream ->
        FileOutputStream(tempFile).use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }
    return tempFile
}


fun uploadMediaToOSS(context: Context, mediaUri: Uri, objectKey: String, onUploaded: (String) -> Unit) {
    val endpoint = "oss-cn-beijing.aliyuncs.com"
    val bucketName = "bitcommunity-test"

    // 注意：出于安全考虑，AccessKeyId和AccessKeySecret不应该直接硬编码在应用中
    // 这里只是示例，实际应用中应该通过安全的方式从您的服务器获取
    val accessKeyId = "LTAI5tLhXN9MnfqPF1g6Ezxn"
    val accessKeySecret = "TizYnYkeydzrFb9BDVoRBGZ99e9I8c"
    val credentialProvider: OSSCredentialProvider = OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, "")

    val conf = ClientConfiguration()
    val oss: OSS = OSSClient(context, endpoint, credentialProvider, conf)

    val file = createFileFromContentUri(context, mediaUri)
    if (file != null) {
        val put = PutObjectRequest(bucketName, objectKey, file.path)

        // 根据文件是视频还是图片设置不同的回调逻辑或处理逻辑
        oss.asyncPutObject(put, object : OSSCompletedCallback<PutObjectRequest, PutObjectResult> {
            override fun onSuccess(request: PutObjectRequest?, result: PutObjectResult?) {
                val mediaUrl = oss.presignPublicObjectURL(bucketName, objectKey)
                onUploaded(mediaUrl)
            }

            override fun onFailure(request: PutObjectRequest?, clientException: ClientException?, serviceException: ServiceException?) {
                // 处理上传失败的情况，这里的逻辑不变
                Log.e("Upload", "上传失败: ${clientException?.message}")
            }
        })
    }
}

fun createFileFromImageUri(context: Context, contentUri: Uri): File? {
    val contentResolver = context.contentResolver
    val fileExtension = contentResolver.getType(contentUri)?.substringAfterLast('/')
    val tempFile = File.createTempFile("upload_", ".$fileExtension", context.cacheDir)
    tempFile.deleteOnExit()
    contentResolver.openInputStream(contentUri)?.use { inputStream ->
        FileOutputStream(tempFile).use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }
    return tempFile
}


fun uploadImageToOSS(context: Context, imageUri: Uri, objectKey: String, onUploaded: (String) -> Unit) {
    val endpoint = "oss-cn-beijing.aliyuncs.com"
    val bucketName = "bitcommunity-test"
    val accessKeyId = "LTAI5tLhXN9MnfqPF1g6Ezxn"
    val accessKeySecret = "TizYnYkeydzrFb9BDVoRBGZ99e9I8c"
    val credentialProvider: OSSCredentialProvider = OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, "")

    val conf = ClientConfiguration()
    val oss: OSS = OSSClient(context, endpoint, credentialProvider, conf)

    val file = createFileFromImageUri(context, imageUri)
    if (file != null) {
        val put = PutObjectRequest(bucketName, objectKey, file.path)
        oss.asyncPutObject(put, object : OSSCompletedCallback<PutObjectRequest, PutObjectResult> {
            override fun onSuccess(request: PutObjectRequest?, result: PutObjectResult?) {
                val imageUrl = oss.presignPublicObjectURL(bucketName, objectKey)
                onUploaded(imageUrl) // 调用回调函数
            }

            override fun onFailure(request: PutObjectRequest?, clientException: ClientException?, serviceException: ServiceException?) {
                Log.e("Upload", "上传失败: ${clientException?.message}")
            }
        })
    }
}


fun playVideo(context: Context, videoUri: Uri?) {
    videoUri?.let {
        val intent = Intent(Intent.ACTION_VIEW, it)
        intent.setDataAndType(it, "video/*")
        context.startActivity(intent)
    }
}
