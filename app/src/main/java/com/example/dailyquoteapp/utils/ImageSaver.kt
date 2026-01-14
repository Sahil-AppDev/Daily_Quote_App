package com.example.dailyquoteapp.utils


import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object ImageSaver {

    fun saveToGallery(
        context: Context,
        bitmap: Bitmap,
        filename: String
    ) {
        val file = File(
            context.getExternalFilesDir(null),
            "$filename.png"
        )

        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }

    // ✅ THIS IS THE MISSING METHOD
    fun saveTempAndGetUri(
        context: Context,
        bitmap: Bitmap
    ): Uri {
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()

        val file = File(cachePath, "share_quote.png")

        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
}
