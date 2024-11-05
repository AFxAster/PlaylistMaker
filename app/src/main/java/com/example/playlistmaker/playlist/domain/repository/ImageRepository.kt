package com.example.playlistmaker.playlist.domain.repository

interface ImageRepository {
    fun saveImage(pathFrom: String, id: String)
    fun getImagePathById(id: String): String
}