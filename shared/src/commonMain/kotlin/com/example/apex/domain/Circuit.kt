package com.example.apex.domain

import androidx.compose.runtime.Immutable

@Immutable
data class Circuit(
    val id: Int,
    val name: String,
    val city: String,
    val country: String
)
