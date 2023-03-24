package com.example.core_common.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider.getUriForFile
import java.io.File

object FileProviderUtils {
    internal val TAG = "FileProviderUtils"

    fun getUri(context: Context, file: File): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getUriForFile(context, context.packageName + ".template.fileprovider", file)
        } else {
            Uri.fromFile(file)
        }
    }

    fun getUri(context: Context, filePath: String): Uri {
        return getUri(context, File(filePath))
    }

    fun fillIntent(context: Context, file: File, intent: Intent, canWrite: Boolean) {
        val uri = getUri(context, file)
        val fileName = file.name
        var extension = ""
        if (!TextUtils.isEmpty(fileName)) {
            val index = fileName.lastIndexOf(".")
            if (index > 0) {
                extension = fileName.substring(index + 1)
            }
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        if (canWrite) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        var mimeType = "*/*"
        if (!TextUtils.isEmpty(extension)) {
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension).toString()
        }
        intent.setDataAndType(uri, mimeType)
    }

    fun fillIntent(context: Context, file: File, intent: Intent, mimeType: String, canWrite: Boolean) {
        val uri = getUri(context, file)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        if (canWrite) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        if (TextUtils.isEmpty(mimeType)) {
            intent.data = uri
        } else {
            intent.setDataAndType(uri, mimeType)
        }
    }
}