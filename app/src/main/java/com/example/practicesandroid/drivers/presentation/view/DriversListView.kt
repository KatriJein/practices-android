package com.example.practicesandroid.drivers.presentation.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.practicesandroid.drivers.presentation.model.DriverUIModel
import com.example.practicesandroid.drivers.presentation.model.DriversViewState
import com.example.practicesandroid.drivers.presentation.model.Team
import com.example.practicesandroid.uikit.Dimens
import com.example.practicesandroid.drivers.presentation.ui.DriverImages
import com.example.practicesandroid.drivers.presentation.ui.TeamColorsRes
import com.example.practicesandroid.drivers.presentation.viewModel.DriversViewModel
import com.example.practicesandroid.ui.theme.PracticesAndroidTheme
import com.example.practicesandroid.uikit.FullscreenError
import com.example.practicesandroid.uikit.FullscreenLoading
import org.koin.androidx.compose.koinViewModel

@Composable
fun DriversListView() {
    val viewModel = koinViewModel<DriversViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val sortOrder by viewModel.sortOrder.collectAsStateWithLifecycle()
    val showSortBadge by viewModel.showSortBadge.collectAsStateWithLifecycle()

    DriversScreenContent(
        state = state.state,
        sortOrder = sortOrder,
        onDriverClick = viewModel::onDriverClick,
        onRetryClick = viewModel::onRetryClick,
        onSortClick = viewModel::onSortClick,
        showSortBadge = showSortBadge
    )
}

@Composable
private fun DriversScreenContent(
    state: DriversViewState.State,
    sortOrder: Boolean,
    onDriverClick: (DriverUIModel) -> Unit = {},
    onRetryClick: () -> Unit = {},
    onSortClick: () -> Unit = {},
    showSortBadge: Boolean
) {
    Scaffold(
        topBar = {
            DriversTopAppBar(
                sortOrder = sortOrder, onSortClick = onSortClick, showBadge = showSortBadge
            )
        }, contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        when (state) {
            is DriversViewState.State.Loading -> {
                FullscreenLoading()
            }

            is DriversViewState.State.Error -> {
                FullscreenError(
                    retry = { onRetryClick() },
                    text = state.error,
                )
            }

            is DriversViewState.State.Success -> {
                DriversList(
                    drivers = state.data,
                    onDriverClick = onDriverClick,
                    modifier = Modifier.padding(paddingValues),
                    sortOrder = sortOrder,
                )
            }
        }
    }
}

@Composable
fun DriversList(
    drivers: List<DriverUIModel>,
    onDriverClick: (DriverUIModel) -> Unit,
    modifier: Modifier = Modifier,
    sortOrder: Boolean
) {
    val listState = rememberLazyListState()

    LaunchedEffect(sortOrder) {
        listState.animateScrollToItem(0)
    }

    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(horizontal = Dimens.Large),
        verticalArrangement = Arrangement.spacedBy(Dimens.Small)
    ) {
        items(
            items = drivers, key = { it.id ?: "" }) { driver ->
            DriversListItem(
                driver = driver, onClick = { onDriverClick(driver) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriversTopAppBar(
    sortOrder: Boolean,
    onSortClick: () -> Unit,
    showBadge: Boolean,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = "Список пилотов",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Medium
                ),

                )
        }, actions = {
            BadgedBox(
                badge = {
                    if (showBadge) {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(8.dp)
                        )
                    }
                }) {
                IconButton(onClick = onSortClick) {
                    Icon(
                        imageVector = if (sortOrder) {
                            Icons.Outlined.ArrowUpward
                        } else {
                            Icons.Outlined.ArrowDownward
                        },
                        contentDescription = if (sortOrder) {
                            "Сортировка по возрастанию"
                        } else {
                            "Сортировка по убыванию"
                        },
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }, modifier = modifier
    )
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
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Dimens.Small)
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
                text = driver.team?.name ?: "", style = MaterialTheme.typography.bodyMedium
            )
        }

        Text(
            text = driver.points.toString(),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(end = 8.dp)
        )
    }
}

@Preview(showBackground = true, name = "Success State")
@Composable
fun DriversScreenContentSuccessPreview() {
    PracticesAndroidTheme {
        DriversScreenContent(
            state = DriversViewState.State.Success(
                data = listOf(
                    DriverUIModel(
                        id = "1",
                        name = "Льюис",
                        surname = "Хэмилтон",
                        number = 44,
                        points = 256,
                        team = Team(
                            id = "mercedes", name = "Mercedes", nationality = "Great Britain",
                            year = 1966,
                        ),
                        nationality = "Great Britain",
                        position = 1,
                        wins = 7,
                        birthday = "19/11/2001"
                    ), DriverUIModel(
                        id = "2",
                        name = "Макс",
                        surname = "Ферстаппен",
                        number = 33,
                        points = 244,
                        team = Team(
                            id = "red_bull",
                            name = "Red Bull Racing",
                            nationality = "Great Britain",
                            year = 1966
                        ),
                        nationality = "Netherlands",
                        position = 2,
                        wins = 6,
                        birthday = "19/11/2001"
                    ), DriverUIModel(
                        id = "3",
                        name = "Шарль",
                        surname = "Леклер",
                        number = 16,
                        points = 198,
                        team = Team(
                            id = "ferrari",
                            name = "Ferrari",
                            nationality = "Great Britain",
                            year = 1966
                        ),
                        nationality = "Monaco",
                        position = 3,
                        wins = 3,
                        birthday = "19/11/2001"
                    )
                )
            ),
            sortOrder = true, onDriverClick = { }, onRetryClick = { }, onSortClick = { },
            showSortBadge = true,
        )
    }
}