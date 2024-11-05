package com.example.playlistmaker.playlist.domain.interactor

import com.example.playlistmaker.playlist.domain.api.ImageInteractor
import com.example.playlistmaker.playlist.domain.repository.ImageRepository

class ImageInteractorImpl(
    private val imageRepository: ImageRepository
) : ImageInteractor {
    override fun saveImage(pathFrom: String, id: String) {
        imageRepository.saveImage(pathFrom, id)
    }

    override fun getImagePathById(id: String): String =
        imageRepository.getImagePathById(id)
}