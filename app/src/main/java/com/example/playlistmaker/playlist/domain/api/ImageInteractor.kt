package com.example.playlistmaker.playlist.domain.api

interface ImageInteractor {
    fun saveImage(pathFrom: String, id: Long)
    fun getImagePathById(id: Long): String
}