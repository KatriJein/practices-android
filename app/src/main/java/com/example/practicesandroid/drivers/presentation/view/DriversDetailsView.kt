package com.example.practicesandroid.drivers.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.practicesandroid.drivers.presentation.model.DriverDetailsUIModel
import com.example.practicesandroid.drivers.presentation.model.DriverDetailsViewState
import com.example.practicesandroid.drivers.presentation.ui.CountryCodes
import com.example.practicesandroid.uikit.Dimens
import com.example.practicesandroid.drivers.presentation.ui.DriverImages
import com.example.practicesandroid.drivers.presentation.ui.TeamColorsRes
import com.example.practicesandroid.drivers.presentation.ui.TeamLogos
import com.example.practicesandroid.drivers.presentation.viewModel.DriversDetailsViewModel
import com.example.practicesandroid.uikit.FullscreenError
import com.example.practicesandroid.uikit.FullscreenLoading
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

@Composable
fun DriversDetailsView(
    driverId: String,
    initialPoints: Int? = null,
    initialPosition: Int? = null,
    initialWins: Int? = null
) {
    val viewModel = koinViewModel<DriversDetailsViewModel> {
        parametersOf(driverId, initialPoints, initialPosition, initialWins)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()

    val context = LocalContext.current

    DriversDetailsScreenContent(
        state = state.state,
        isFavorite = isFavorite,
        onRetryClick = viewModel::onRetryClick,
        onFavoriteClick = viewModel::onFavoriteClick,
        onShareClick = { viewModel.onShareClick(context) },
        onBackClick = viewModel::onBackClick
    )
}

@Composable
private fun DriversDetailsScreenContent(
    state: DriverDetailsViewState.State,
    isFavorite: Boolean,
    onRetryClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            DriverDetailsTopAppBar(
                isFavorite = isFavorite,
                onFavoriteClick = onFavoriteClick,
                onShareClick = onShareClick,
                onBackClick = onBackClick
            )
        }, contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        when (state) {
            is DriverDetailsViewState.State.Loading -> {
                FullscreenLoading()
            }

            is DriverDetailsViewState.State.Error -> {
                FullscreenError(
                    retry = { onRetryClick() },
                    text = state.message,
                )

            }

            is DriverDetailsViewState.State.Success -> {
                DriversDetailsContent(
                    driver = state.driver,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
fun DriversDetailsContent(
    driver: DriverDetailsUIModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        DriverHeader(driver)
        DriverStats(driver)
        PersonalInfo(driver)
        TeamInfo(driver)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DriverHeader(driver: DriverDetailsUIModel) {
    GlideImage(
        model = DriverImages.getImageUrl(driver.id),
        contentDescription = "${driver.name} ${driver.surname}",
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(bottomStart = Dimens.Large, bottomEnd = Dimens.Large)),
        contentScale = ContentScale.Crop
    )

    Column(
        modifier = Modifier.padding(Dimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.Medium)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${driver.name} ${driver.surname}",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${driver.number}",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                color = TeamColorsRes.getColor(driver.team?.id ?: "")
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverDetailsTopAppBar(
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад"
                )
            }
        },
        actions = {
            IconButton(onClick = onShareClick) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Поделиться"
                )
            }

            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) {
                        Icons.Default.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    },
                    contentDescription = if (isFavorite) {
                        "Удалить из избранного"
                    } else {
                        "Добавить в избранное"
                    },
                    tint = if (isFavorite) {
                        Color.Red
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun DriverStats(driver: DriverDetailsUIModel) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color.Transparent,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.ScreenPadding),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(icon = Icons.Default.Star, label = "Очки", value = driver.points)
            StatItem(
                icon = Icons.Default.EmojiEvents,
                label = "Позиция",
                value = driver.position
            )
            StatItem(
                icon = Icons.Default.MilitaryTech,
                label = "Победы",
                value = driver.wins
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PersonalInfo(driver: DriverDetailsUIModel) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(Dimens.ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.Medium)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                driver.nationality?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.width(Dimens.Medium))

                val flagUrl = CountryCodes.getFlagUrl(driver.nationality)
                GlideImage(
                    model = flagUrl,
                    contentDescription = "Флаг ${driver.nationality}",
                    modifier = Modifier
                        .size(32.dp)

                )
            }

            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val age = driver.birthday?.let { birthdayString ->
                try {
                    val birthday = LocalDate.parse(birthdayString, formatter)
                    Period.between(birthday, LocalDate.now()).years
                } catch (e: Exception) {
                    null
                }
            }
            Text(
                text = buildAnnotatedString {
                    append("Дата рождения: ${driver.birthday}")
                    age?.let {
                        append(" (${it} ${getAgeSuffix(it)})")
                    }
                },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun getAgeSuffix(age: Int): String {
    return when {
        age % 10 == 1 && age % 100 != 11 -> "год"
        age % 10 in 2..4 && age % 100 !in 12..14 -> "года"
        else -> "лет"
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TeamInfo(driver: DriverDetailsUIModel) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color.Transparent,
    ) {
        Column(
            modifier = Modifier.padding(Dimens.ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.Medium)
        ) {
            driver.team?.name?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
                )
            }

            GlideImage(
                model = TeamLogos.getLogo(driver.team?.id),
                contentDescription = driver.team?.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentScale = ContentScale.Fit
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                driver.team?.nationality?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.width(Dimens.Medium))

                val flagUrl = CountryCodes.getFlagUrl(driver.team?.nationality)
                GlideImage(
                    model = flagUrl,
                    contentDescription = "Флаг ${driver.team?.nationality}",
                    modifier = Modifier
                        .size(32.dp)

                )
            }
            Text(
                "Дата первого появления: ${driver.team?.year}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun StatItem(icon: ImageVector, label: String, value: Int?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.Small)
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = Color(0xFFFFC107),
            modifier = Modifier.size(30.dp)
        )
        Text(
            text = "$value",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
    }
}



