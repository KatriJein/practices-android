package com.example.practicesandroid.account.presentation.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.practicesandroid.account.presentation.model.AccountViewState
import com.example.practicesandroid.account.presentation.model.FavoriteDriverUIModel
import com.example.practicesandroid.account.presentation.viewModel.AccountViewModel
import com.example.practicesandroid.drivers.presentation.ui.DriverImages
import com.example.practicesandroid.drivers.presentation.ui.TeamColorsRes
import com.example.practicesandroid.uikit.Dimens
import com.example.practicesandroid.uikit.FullscreenError
import com.example.practicesandroid.uikit.FullscreenLoading
import org.koin.androidx.compose.koinViewModel

@Composable
fun AccountView() {
    val viewModel = koinViewModel<AccountViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    AccountScreenContent(
        state = state.state,
        onDriverClick = viewModel::onDriverClick,
    )
}

@Composable
private fun AccountScreenContent(
    state: AccountViewState.State,
    onDriverClick: (FavoriteDriverUIModel) -> Unit = {},
) {
    when (state) {
        is AccountViewState.State.Loading -> {
            FullscreenLoading()
        }

        is AccountViewState.State.Error -> {
            FullscreenError(
                retry = { }, text = state.error
            )
        }

        is AccountViewState.State.Success -> {
            AccountScreen(
                drivers = state.favouritesDrivers,
                onDriverClick = onDriverClick,
            )
        }
    }
}

@Composable
private fun AccountScreen(
    drivers: List<FavoriteDriverUIModel>,
    onDriverClick: (FavoriteDriverUIModel) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.Large)
    ) {
        Text(
            text = "Избранные пилоты", style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold
            ), modifier = Modifier.padding(
                top = Dimens.Large, bottom = Dimens.Medium
            )
        )

        if (drivers.isEmpty()) {
            EmptyFavoritesState()
        } else {
            FavoriteDriversList(
                drivers = drivers, onDriverClick = onDriverClick
            )
        }
    }
}

@Composable
private fun EmptyFavoritesState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.FavoriteBorder,
            contentDescription = "Нет избранных",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(Dimens.Medium))
        Text(
            text = "Нет избранных пилотов",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = "Добавьте пилотов в избранное на странице списка",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = Dimens.Large)
        )
    }
}

@Composable
private fun FavoriteDriversList(
    drivers: List<FavoriteDriverUIModel>,
    onDriverClick: (FavoriteDriverUIModel) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Dimens.Small)
    ) {
        items(
            items = drivers, key = { it.id ?: "" }) { driver ->
            FavoriteDriverListItem(
                driver = driver, onClick = { onDriverClick(driver) })
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun FavoriteDriverListItem(
    driver: FavoriteDriverUIModel,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.Small)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            model = DriverImages.getImageUrl(driver.id),
            contentDescription = "${driver.name} ${driver.surname}",
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(Dimens.Large)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Dimens.Small)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.Medium)
            ) {
                Text(
                    text = "${driver.name} ${driver.surname}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = driver.number.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TeamColorsRes.getColor("")
                )
            }
            Text(
                text = driver.teamName ?: "", style = MaterialTheme.typography.bodyMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.Medium)
            ) {
                driver.points?.let { points ->
                    Text(
                        text = "Очки: $points",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                driver.position?.let { position ->
                    Text(
                        text = "Позиция: $position",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}