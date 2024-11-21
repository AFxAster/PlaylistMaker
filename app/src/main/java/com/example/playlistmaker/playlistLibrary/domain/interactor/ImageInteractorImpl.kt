package com.example.playlistmaker.playlistLibrary.domain.interactor

import com.example.playlistmaker.playlistLibrary.domain.api.ImageInteractor
import com.example.playlistmaker.playlistLibrary.domain.repository.ImageRepository

class ImageInteractorImpl(
    private val imageRepository: ImageRepository
) : ImageInteractor {
    override fun saveImage(pathFrom: String, id: Long) {
        imageRepository.saveImage(pathFrom, id)
    }

    override fun getImagePathById(id: Long): String =
        imageRepository.getImagePathById(id)
}