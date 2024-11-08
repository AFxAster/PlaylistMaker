package com.example.playlistmaker.playlist.domain.interactor

import com.example.playlistmaker.playlist.domain.api.ImageInteractor
import com.example.playlistmaker.playlist.domain.repository.ImageRepository

class ImageInteractorImpl(
    private val imageRepository: ImageRepository
) : ImageInteractor {
    override fun saveImage(pathFrom: String, id: Long) {
        imageRepository.saveImage(pathFrom, id)
    }

    override fun getImagePathById(id: Long): String =
        imageRepository.getImagePathById(id)
}