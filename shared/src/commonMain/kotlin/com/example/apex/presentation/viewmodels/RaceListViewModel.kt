package com.example.apex.presentation.viewmodels

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apex.data.RaceRepository
import com.example.apex.domain.Race
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Immutable
data class RaceListUiState(
    val races: List<Race> = emptyList(),
    val filteredRaces: List<Race> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isPagingLoading: Boolean = false,
    val isError: Boolean = false,
    val endReached: Boolean = false
)

class RaceListViewModel(
    private val repository: RaceRepository
) : ViewModel() {

    private val pageSize = 10
    private var currentOffset = 0

    private val _uiState = MutableStateFlow(
        RaceListUiState(isLoading = true)
    )

    val uiState: StateFlow<RaceListUiState> = _uiState.asStateFlow()

    init {
        loadInitialRaces()
    }

    fun loadInitialRaces() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isError = false
            )
            try {
                currentOffset = 0
                val races = repository.getRaces(limit = pageSize, offset = currentOffset)
                currentOffset += races.size
                val endReached = races.size < pageSize

                val filtered = filterRaces(races, _uiState.value.searchQuery)
                _uiState.value = _uiState.value.copy(
                    races = races,
                    filteredRaces = filtered,
                    isLoading = false,
                    isError = false,
                    endReached = endReached
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isError = true
                )
            }
        }
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isLoading || state.isPagingLoading || state.endReached || state.isError) {
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isPagingLoading = true)
            try {
                val newRaces = repository.getRaces(limit = pageSize, offset = currentOffset)
                currentOffset += newRaces.size
                val endReached = newRaces.size < pageSize

                val totalRaces = state.races + newRaces
                val filtered = filterRaces(totalRaces, state.searchQuery)

                _uiState.value = _uiState.value.copy(
                    races = totalRaces,
                    filteredRaces = filtered,
                    isPagingLoading = false,
                    endReached = endReached
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                _uiState.value = _uiState.value.copy(
                    isPagingLoading = false
                )
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        val currentRaces = _uiState.value.races
        val filtered = filterRaces(currentRaces, query)
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            filteredRaces = filtered
        )
    }

    private fun filterRaces(races: List<Race>, query: String): List<Race> {
        if (query.isBlank()) return races
        return races.filter { race ->
            race.name.contains(query, ignoreCase = true) ||
                    race.circuitName.contains(query, ignoreCase = true) ||
                    race.country.contains(query, ignoreCase = true) ||
                    race.city.contains(query, ignoreCase = true)
        }
    }
}
