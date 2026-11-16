package com.example.apex.data

import com.example.apex.data.remote.F1ApiService
import com.example.apex.domain.Circuit
import com.example.apex.domain.Driver
import com.example.apex.domain.Race
import com.example.apex.domain.RaceDetails
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KtorRaceRepository(
    private val apiService: F1ApiService = F1ApiService()
) : RaceRepository {

    private var racesCache: List<Race>? = null
    private val detailsCache = mutableMapOf<Int, RaceDetails>()
    private val circuitCache = mutableMapOf<Int, Circuit>()

    override suspend fun getRaces(): List<Race> {
        racesCache?.let { return it }

        return withContext(Dispatchers.Default) {
            try {
                val response = apiService.getRaces()
                val raceDtos = response.mrData.raceTable?.races ?: emptyList()

                val mapped = raceDtos.mapIndexed { index, dto ->
                    val roundInt = dto.round?.toIntOrNull() ?: (index + 1)
                    val circuitIntId = dto.circuit?.circuitId?.hashCode() ?: roundInt

                    val circuit = Circuit(
                        id = circuitIntId,
                        name = dto.circuit?.circuitName ?: "",
                        city = dto.circuit?.location?.locality ?: "",
                        country = dto.circuit?.location?.country ?: ""
                    )
                    circuitCache[circuitIntId] = circuit

                    Race(
                        id = roundInt,
                        name = dto.raceName ?: "",
                        circuitId = circuitIntId,
                        circuitName = dto.circuit?.circuitName ?: "",
                        city = dto.circuit?.location?.locality ?: "",
                        country = dto.circuit?.location?.country ?: "",
                        date = dto.date ?: "",
                        round = roundInt
                    )
                }

                racesCache = mapped
                mapped
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                emptyList()
            }
        }
    }

    override suspend fun getRaceDetails(id: Int): RaceDetails? {
        detailsCache[id]?.let { return it }

        return withContext(Dispatchers.Default) {
            try {
                val races = getRaces()
                val race = races.find { it.id == id } ?: return@withContext null

                val circuit = getCircuit(race.circuitId) ?: Circuit(
                    id = race.circuitId,
                    name = race.circuitName,
                    city = race.city,
                    country = race.country
                )

                val response = apiService.getRaceResults(round = id)
                val raceWithResults = response.mrData.raceTable?.races?.firstOrNull()
                val results = raceWithResults?.results ?: emptyList()

                val drivers = results.mapIndexed { index, resDto ->
                    val driverDto = resDto.driver
                    val driverIdInt = driverDto?.driverId?.hashCode() ?: (index + 1)
                    val num = driverDto?.permanentNumber?.toIntOrNull()
                        ?: resDto.number?.toIntOrNull()
                        ?: (index + 1)

                    Driver(
                        id = driverIdInt,
                        firstName = driverDto?.givenName ?: "",
                        lastName = driverDto?.familyName ?: "",
                        number = num
                    )
                }

                val details = RaceDetails(
                    race = race,
                    circuit = circuit,
                    drivers = drivers
                )

                detailsCache[id] = details
                details
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                null
            }
        }
    }

    override suspend fun getCircuit(id: Int): Circuit? {
        circuitCache[id]?.let { return it }

        return withContext(Dispatchers.Default) {
            try {
                getRaces()
                circuitCache[id]
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                null
            }
        }
    }

    override suspend fun getDrivers(): List<Driver> {
        return emptyList()
    }
}