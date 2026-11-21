package com.example.apex.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.apex.presentation.screens.components.RaceCard
import com.example.apex.presentation.viewmodels.RaceListUiState
import f1archive.shared.generated.resources.Res
import f1archive.shared.generated.resources.error_loading
import f1archive.shared.generated.resources.loading
import f1archive.shared.generated.resources.nothing_found
import f1archive.shared.generated.resources.retry
import f1archive.shared.generated.resources.search_races
import org.jetbrains.compose.resources.stringResource

@Composable
fun RaceListScreen(
    state: RaceListUiState,
    onSearchQueryChange: (String) -> Unit,
    onRaceClick: (Int) -> Unit,
    onLoadNextPage: () -> Unit,
    onRetry: () -> Unit
) {
    val listState = rememberLazyListState()

    val shouldLoadMore by remember(state.isLoading, state.isPagingLoading, state.endReached, state.isError) {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 0 && lastVisibleItemIndex >= totalItems - 2 && !state.isLoading && !state.isPagingLoading && !state.endReached && !state.isError
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadNextPage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    text = stringResource(Res.string.search_races)
                )
            },
            singleLine = true
        )

        when {
            state.isLoading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = stringResource(Res.string.loading),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }

            state.isError && state.races.isEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = stringResource(Res.string.error_loading))
                    Button(
                        onClick = onRetry,
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(text = stringResource(Res.string.retry))
                    }
                }
            }

            state.filteredRaces.isEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = stringResource(Res.string.nothing_found))
                }
            }

            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(
                        items = state.filteredRaces,
                        key = { it.id }
                    ) { race ->
                        RaceCard(
                            race = race,
                            onClick = { onRaceClick(race.id) }
                        )
                    }

                    if (state.isPagingLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}
