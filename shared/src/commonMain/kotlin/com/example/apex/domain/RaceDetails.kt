package com.example.apex.domain

import androidx.compose.runtime.Immutable

@Immutable
data class RaceDetails(
    val race: Race,
    val circuit: Circuit,
    val drivers: List<Driver>
)
