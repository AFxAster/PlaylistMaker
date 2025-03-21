package com.example.playlistmaker.settings.domain.interactor

import com.example.playlistmaker.settings.domain.api.SharingInteractor
import com.example.playlistmaker.settings.domain.repository.LinkRepository
import com.example.playlistmaker.settings.domain.repository.SharingRepository

class SharingInteractorImpl(
    private val sharingRepository: SharingRepository,
    private val linkRepository: LinkRepository
) : SharingInteractor {
    override fun shareApp() {
        sharingRepository.shareText(linkRepository.getShareAppLink())
    }

    override fun shareText(text: String) {
        sharingRepository.shareText(text)
    }

    override fun openTerms() {
        sharingRepository.openLink(linkRepository.getTermsLink())
    }

    override fun openSupport() {
        sharingRepository.openEmail(linkRepository.getSupportEmail())
    }
}