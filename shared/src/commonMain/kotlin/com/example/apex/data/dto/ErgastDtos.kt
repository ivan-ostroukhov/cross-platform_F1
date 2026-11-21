package com.example.apex.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErgastResponseDto<T>(
    @SerialName("MRData") val mrData: T
)

@Serializable
data class RaceTableDataDto(
    @SerialName("limit") val limit: String? = null,
    @SerialName("offset") val offset: String? = null,
    @SerialName("total") val total: String? = null,
    @SerialName("RaceTable") val raceTable: RacesWrapperDto? = null
)

@Serializable
data class RacesWrapperDto(
    @SerialName("season") val season: String? = null,
    @SerialName("Races") val races: List<RaceDto> = emptyList()
)

@Serializable
data class RaceDto(
    @SerialName("season") val season: String? = null,
    @SerialName("round") val round: String? = null,
    @SerialName("url") val url: String? = null,
    @SerialName("raceName") val raceName: String? = null,
    @SerialName("Circuit") val circuit: CircuitDto? = null,
    @SerialName("date") val date: String? = null,
    @SerialName("time") val time: String? = null,
    @SerialName("Results") val results: List<ResultDto>? = null
)

@Serializable
data class CircuitDto(
    @SerialName("circuitId") val circuitId: String? = null,
    @SerialName("url") val url: String? = null,
    @SerialName("circuitName") val circuitName: String? = null,
    @SerialName("Location") val location: LocationDto? = null
)

@Serializable
data class LocationDto(
    @SerialName("lat") val lat: String? = null,
    @SerialName("long") val long: String? = null,
    @SerialName("locality") val locality: String? = null,
    @SerialName("country") val country: String? = null
)

@Serializable
data class ResultDto(
    @SerialName("number") val number: String? = null,
    @SerialName("position") val position: String? = null,
    @SerialName("Driver") val driver: DriverDto? = null
)

@Serializable
data class DriverDto(
    @SerialName("driverId") val driverId: String? = null,
    @SerialName("permanentNumber") val permanentNumber: String? = null,
    @SerialName("code") val code: String? = null,
    @SerialName("givenName") val givenName: String? = null,
    @SerialName("familyName") val familyName: String? = null,
    @SerialName("dateOfBirth") val dateOfBirth: String? = null,
    @SerialName("nationality") val nationality: String? = null
)

@Serializable
data class CircuitTableDataDto(
    @SerialName("CircuitTable") val circuitTable: CircuitsWrapperDto? = null
)

@Serializable
data class CircuitsWrapperDto(
    @SerialName("Circuits") val circuits: List<CircuitDto> = emptyList()
)