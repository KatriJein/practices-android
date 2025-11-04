package com.example.practicesandroid.drivers.domain.interactor

import com.example.practicesandroid.account.data.repository.FavoriteDriversRepository
import com.example.practicesandroid.drivers.data.cache.BadgeCacheManager
import com.example.practicesandroid.drivers.data.entity.FavoriteDriverEntity
import com.example.practicesandroid.drivers.data.repository.DriversRepository
import com.example.practicesandroid.drivers.presentation.model.DriverDetailsUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class DriversInteractor(
    private val driversRepository: DriversRepository,
    private val favoriteDriversRepository: FavoriteDriversRepository,
    private val badgeCacheManager: BadgeCacheManager
) {
    suspend fun getDrivers() = driversRepository.getDrivers()

    suspend fun getDriverById(driverId: String) = driversRepository.getDriverById(driverId)

    suspend fun toggleSortOrder() {
        val currentOrder = driversRepository.sortOrderFlow.first()
        val newOrder = !currentOrder
        driversRepository.updateSortOrder(newOrder)

        badgeCacheManager.setBadgeVisible(DRIVERS_SORT_BADGE_KEY, newOrder)
    }

    fun getSortOrderFlow(): Flow<Boolean> = driversRepository.sortOrderFlow

    fun shouldShowSortBadge(): Boolean {
        return badgeCacheManager.isBadgeVisible(DRIVERS_SORT_BADGE_KEY)
    }

    suspend fun toggleFavorite(driver: DriverDetailsUIModel) {
        val favoriteEntity = FavoriteDriverEntity(
            id = driver.id ?: "",
            name = driver.name ?: "",
            surname = driver.surname ?: "",
            number = driver.number,
            teamName = driver.team?.name,
            points = driver.points,
            position = driver.position,
            wins = driver.wins,
            nationality = driver.nationality,
            birthday = driver.birthday
        )
        favoriteDriversRepository.toggleFavorite(favoriteEntity)
    }

    suspend fun removeFromFavorites(driverId: String) {
        favoriteDriversRepository.removeFromFavorites(driverId)
    }

    suspend fun isFavorite(driverId: String): Boolean =
        favoriteDriversRepository.isFavorite(driverId)

    fun getFavorites(): Flow<List<FavoriteDriverEntity>> =
        favoriteDriversRepository.getFavorites()

    companion object {
        private const val DRIVERS_SORT_BADGE_KEY = "drivers_sort_badge"
    }
}
