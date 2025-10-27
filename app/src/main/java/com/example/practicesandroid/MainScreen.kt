package com.example.practicesandroid

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.practicesandroid.navigation.Route
import com.example.practicesandroid.navigation.TopLevelBackStack
import androidx.compose.material.icons.filled.SportsMotorsports
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
//import com.example.practicesandroid.drivers.presentation.MockData
import com.example.practicesandroid.drivers.presentation.view.DriversDetailsView
import com.example.practicesandroid.drivers.presentation.view.DriversListView
import com.example.practicesandroid.ui.theme.PracticesAndroidTheme
import org.koin.compose.koinInject
import org.koin.java.KoinJavaComponent.inject

interface TopLevelRoute : Route {
    val icon: ImageVector
}

data object Home : TopLevelRoute {
    override val icon = Icons.Default.Home
}

data object Races : TopLevelRoute {
    override val icon = Icons.Default.DateRange
}

data object Drivers : TopLevelRoute {
    override val icon = Icons.Default.SportsMotorsports
}

data class DriverDetails(
    val driverId: String,
    val initialPoints: Int? = null,
    val initialPosition: Int? = null,
    val initialWins: Int? = null
) : Route

@Composable
fun MainScreen() {
    val topLevelBackStack by inject<TopLevelBackStack<Route>>(clazz = TopLevelBackStack::class.java)
    Scaffold(bottomBar = {
        NavigationBar {
            listOf(Home, Races, Drivers).forEach { route ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            route.icon,
                            contentDescription = null,
                            modifier = Modifier.size(38.dp)
                        )
                    },
                    selected = topLevelBackStack.topLevelKey == route,
                    onClick = {
                        topLevelBackStack.addTopLevel(route)
                    }
                )
            }
        }
    }) { padding ->
        NavDisplay(
            backStack = topLevelBackStack.backStack,
            onBack = { topLevelBackStack.removeLast() },
            modifier = Modifier.padding(padding),
            entryDecorators = listOf(
                rememberSavedStateNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<Home> {
                    ContentBlue("Домашняя")
                }
                entry<Races> {
                    ContentBlue("Гонки")
                }
                entry<Drivers> {
                    DriversListView(topLevelBackStack)
                }
                entry<DriverDetails> { route ->
                    DriversDetailsView(
                        driverId = route.driverId,
                        initialPoints = route.initialPoints,
                        initialPosition = route.initialPosition,
                        initialWins = route.initialWins
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PracticesAndroidTheme {
        MainScreen()
    }
}