package com.example.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsMotorsports

data object Home : TopLevelRoute {
    override val icon = Icons.Default.Home
}

data object Races : TopLevelRoute {
    override val icon = Icons.Default.DateRange
}

data object Drivers : TopLevelRoute {
    override val icon = Icons.Default.SportsMotorsports
}

data object Account : TopLevelRoute {
    override val icon = Icons.Default.Person
}

data object EditProfile : Route

data class DriverDetails(
    val driverId: String,
    val initialPoints: Int? = null,
    val initialPosition: Int? = null,
    val initialWins: Int? = null
) : Route