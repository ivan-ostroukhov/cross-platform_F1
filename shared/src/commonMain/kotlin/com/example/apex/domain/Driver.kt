package com.example.apex.domain

import androidx.compose.runtime.Immutable

@Immutable
data class Driver(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val number: Int
)
