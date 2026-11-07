package com.example.apex.domain

data class RaceDetails(
    val race: Race,
    val circuit: Circuit,
    val drivers: List<Driver>
)