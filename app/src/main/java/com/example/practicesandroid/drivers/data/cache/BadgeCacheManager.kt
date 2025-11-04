package com.example.practicesandroid.drivers.data.cache

class BadgeCacheManager {
    private val badgeStates = mutableMapOf<String, Boolean>()

    fun setBadgeVisible(route: String, visible: Boolean) {
        badgeStates[route] = visible
    }

    fun isBadgeVisible(route: String): Boolean {
        return badgeStates[route] ?: false
    }
}