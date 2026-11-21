package com.example.apex.presentation.viewmodels

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apex.data.RaceRepository
import com.example.apex.domain.RaceDetails
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Immutable
data class RaceDetailsUiState(
    val details: RaceDetails? = null,
    val isLoading: Boolean = false,
    val notFound: Boolean = false,
    val isError: Boolean = false
)

class RaceDetailsViewModel(
    private val raceId: Int,
    private val repository: RaceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        RaceDetailsUiState(isLoading = true)
    )

    val uiState: StateFlow<RaceDetailsUiState> = _uiState.asStateFlow()

    init {
        loadRace()
    }

    fun loadRace() {
        viewModelScope.launch {
            _uiState.value = RaceDetailsUiState(isLoading = true)
            try {
                val details = repository.getRaceDetails(raceId)
                _uiState.value = if (details == null) {
                    RaceDetailsUiState(notFound = true)
                } else {
                    RaceDetailsUiState(details = details)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                _uiState.value = RaceDetailsUiState(isError = true)
            }
        }
    }
}
