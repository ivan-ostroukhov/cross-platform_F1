package com.example.apex.data

import com.example.apex.domain.Circuit
import com.example.apex.domain.Driver
import com.example.apex.domain.Race
import com.example.apex.domain.RaceDetails

interface RaceRepository {

    suspend fun getRaces(): List<Race>

    suspend fun getRaceDetails(id: Int): RaceDetails?

    suspend fun getCircuit(id: Int): Circuit?

    suspend fun getDrivers(): List<Driver>
}