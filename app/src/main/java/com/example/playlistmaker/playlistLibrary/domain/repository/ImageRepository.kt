package com.example.playlistmaker.playlistLibrary.domain.repository

interface ImageRepository {
    fun saveImage(pathFrom: String, id: Long)
    fun getImagePathById(id: Long): String
}