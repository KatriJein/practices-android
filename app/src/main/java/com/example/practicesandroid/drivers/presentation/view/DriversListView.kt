package com.example.practicesandroid.drivers.presentation.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.practicesandroid.drivers.presentation.model.DriverUIModel
import com.example.practicesandroid.drivers.presentation.model.DriversViewState
import com.example.practicesandroid.drivers.presentation.ui.Dimens
import com.example.practicesandroid.drivers.presentation.ui.DriverImages
import com.example.practicesandroid.drivers.presentation.ui.TeamColorsRes
import com.example.practicesandroid.drivers.presentation.viewModel.DriversViewModel
import com.example.practicesandroid.navigation.Route
import com.example.practicesandroid.navigation.TopLevelBackStack
import com.example.practicesandroid.uikit.FullscreenError
import com.example.practicesandroid.uikit.FullscreenLoading
import org.koin.androidx.compose.koinViewModel

@Composable
fun DriversListView(
    topLevelBackStack: TopLevelBackStack<Route>,
) {
    val viewModel = koinViewModel<DriversViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    DriversScreenContent(
        state = state.state,
        onDriverClick = viewModel::onDriverClick,
        onRetryClick = viewModel::onRetryClick,
    )
}

@Composable
private fun DriversScreenContent(
    state: DriversViewState.State,
    onDriverClick: (DriverUIModel) -> Unit = {},
    onRetryClick: () -> Unit = {},
) {
    when (state) {
        is DriversViewState.State.Loading -> {
            FullscreenLoading()
        }

        is DriversViewState.State.Error -> {
            FullscreenError(
                retry = { onRetryClick() },
                text = state.error
            )
        }

        is DriversViewState.State.Success -> {
            DriversList(
                drivers = state.data,
                onDriverClick = onDriverClick,

            )
        }
    }
}

@Composable
fun DriversList(
    drivers: List<DriverUIModel>,
    onDriverClick: (DriverUIModel) -> Unit,
) {
    LazyColumn(
        modifier = Modifier,
        contentPadding = PaddingValues(Dimens.Large),
        verticalArrangement = Arrangement.spacedBy(Dimens.Small)
    ) {
        items(
            items = drivers,
            key = { it.id ?: "" }
        ) { driver ->
            DriversListItem(
                driver = driver,
                onClick = { onDriverClick(driver) }
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DriversListItem(driver: DriverUIModel, onClick: () -> Unit) {
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
                .size(100.dp)
                .clip(RoundedCornerShape(Dimens.Large)),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Dimens.Small)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    Dimens.Medium
                )
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
                    color = TeamColorsRes.getColor(driver.team?.id ?: "")
                )
            }
            Text(
                text = driver.team?.name ?: "",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text(
            text = driver.points.toString(),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(end = 8.dp)
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    DriversListView({})
//}