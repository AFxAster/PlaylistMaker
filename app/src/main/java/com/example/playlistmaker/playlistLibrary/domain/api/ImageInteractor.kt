package com.example.playlistmaker.playlistLibrary.domain.api

interface ImageInteractor {
    fun saveImage(pathFrom: String, id: Long)
    fun getImagePathById(id: Long): String
}