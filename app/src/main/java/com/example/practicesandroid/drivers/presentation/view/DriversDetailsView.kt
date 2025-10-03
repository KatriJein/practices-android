package com.example.practicesandroid.drivers.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.practicesandroid.drivers.presentation.model.Driver
import com.example.practicesandroid.drivers.presentation.model.DriverDetailsViewState
import com.example.practicesandroid.drivers.presentation.model.Team
import com.example.practicesandroid.drivers.presentation.ui.CountryCodes
import com.example.practicesandroid.drivers.presentation.ui.Dimens
import com.example.practicesandroid.drivers.presentation.ui.DriverImages
import com.example.practicesandroid.drivers.presentation.ui.TeamColorsRes
import com.example.practicesandroid.drivers.presentation.ui.TeamLogos
import com.example.practicesandroid.drivers.presentation.viewModel.DriversDetailsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

@Composable
fun DriversDetailsView(driverId: String) {
    val viewModel = koinViewModel<DriversDetailsViewModel>() {
        parametersOf(driverId)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    DriversDetails(state)
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DriversDetails(state: DriverDetailsViewState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        GlideImage(
            model = DriverImages.getImageUrl(state.driver?.id),
            contentDescription = "${state.driver?.name} ${state.driver?.surname}",
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
                    text = "${state.driver?.name} ${state.driver?.surname}",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${state.driver?.number}",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = TeamColorsRes.getColor(state.driver?.team?.id)
                )
            }
            DriverStats(state)
            PersonalInfo(state)
            TeamInfo(state)
        }
    }
}

@Composable
fun DriverStats(state: DriverDetailsViewState) {
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
            StatItem(icon = Icons.Default.Star, label = "Очки", value = state.driver?.points)
            StatItem(
                icon = Icons.Default.EmojiEvents,
                label = "Позиция",
                value = state.driver?.position
            )
            StatItem(
                icon = Icons.Default.MilitaryTech,
                label = "Победы",
                value = state.driver?.wins
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PersonalInfo(state: DriverDetailsViewState) {
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
                state.driver?.nationality?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.width(Dimens.Medium))

                val flagUrl = CountryCodes.getFlagUrl(state.driver?.nationality)
                GlideImage(
                    model = flagUrl,
                    contentDescription = "Флаг ${state.driver?.nationality}",
                    modifier = Modifier
                        .size(32.dp)

                )
            }

            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val age = Period.between(state.driver?.birthday, LocalDate.now()).years
            Text(
                text = "Дата рождения: ${state.driver?.birthday?.format(formatter)} ($age лет)",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TeamInfo(state: DriverDetailsViewState) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color.Transparent,
    ) {
        Column(
            modifier = Modifier.padding(Dimens.ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.Medium)
        ) {
            state.driver?.team?.name?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
                )
            }

            GlideImage(
                model = TeamLogos.getLogo(state.driver?.team?.id),
                contentDescription = state.driver?.team?.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentScale = ContentScale.Fit
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                state.driver?.team?.nationality?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.width(Dimens.Medium))

                val flagUrl = CountryCodes.getFlagUrl(state.driver?.team?.nationality)
                GlideImage(
                    model = flagUrl,
                    contentDescription = "Флаг ${state.driver?.team?.nationality}",
                    modifier = Modifier
                        .size(32.dp)

                )
            }
            Text(
                "Дата первого появления: ${state.driver?.team?.year}",
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


