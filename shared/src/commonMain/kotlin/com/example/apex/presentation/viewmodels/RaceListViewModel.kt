package com.example.apex.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apex.data.RaceRepository
import com.example.apex.domain.Race
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RaceListUiState(
    val races: List<Race> = emptyList(),
    val filteredRaces: List<Race> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false
)

class RaceListViewModel(
    private val repository: RaceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        RaceListUiState(isLoading = true)
    )

    val uiState: StateFlow<RaceListUiState> = _uiState.asStateFlow()

    init {
        loadRaces()
    }

    private fun loadRaces() {
        viewModelScope.launch {
            val races = repository.getRaces()
            _uiState.value = RaceListUiState(
                races = races,
                filteredRaces = races
            )
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