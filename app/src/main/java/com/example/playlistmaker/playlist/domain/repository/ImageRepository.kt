package com.example.playlistmaker.playlist.domain.repository

interface ImageRepository {
    fun saveImage(pathFrom: String, id: Long)
    fun getImagePathById(id: Long): String
}