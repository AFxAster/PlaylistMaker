package com.example.playlistmaker.playlistLibrary.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.playlistLibrary.domain.repository.ImageRepository
import java.io.File
import java.io.FileOutputStream

class ImageRepositoryImpl(
    private val context: Context
) : ImageRepository {
    override fun saveImage(pathFrom: String, id: Long) {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_artwork")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "artwork_$id.jpg")
        val inputStream = context.contentResolver.openInputStream(Uri.parse(pathFrom))
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        inputStream?.close()
        outputStream.close()
    }

    override fun getImagePathById(id: Long): String {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_artwork")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "artwork_$id.jpg")
        return file.path
    }
}