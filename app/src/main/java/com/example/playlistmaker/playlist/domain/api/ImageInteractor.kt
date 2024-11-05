package com.example.playlistmaker.playlist.domain.api

interface ImageInteractor {
    fun saveImage(pathFrom: String, id: String)
    fun getImagePathById(id: String): String
}