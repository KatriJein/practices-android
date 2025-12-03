package com.example.profile.domain.interactor

import com.example.profile.data.repository.ProfileRepository
import com.example.profile.domain.model.ProfileEntity
import kotlinx.coroutines.flow.Flow

class ProfileInteractor(
    private val profileRepository: ProfileRepository
) {
    fun observeProfile(): Flow<ProfileEntity> {
        return profileRepository.observeProfile()
    }

    suspend fun saveProfile(profile: ProfileEntity) {
        profileRepository.setProfile(
            fullName = profile.fullName,
            photoUri = profile.photoUri,
            resumeUrl = profile.resumeUrl,
            time = profile.time
        )
    }
}