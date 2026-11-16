package com.example.apex.data.remote

import com.example.apex.data.ApiConfig
import com.example.apex.data.createHttpClientEngine
import com.example.apex.data.dto.CircuitTableDataDto
import com.example.apex.data.dto.ErgastResponseDto
import com.example.apex.data.dto.RaceTableDataDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class F1ApiService(
    private val baseUrl: String = ApiConfig.baseUrl
) {
    private val client = HttpClient(createHttpClientEngine()) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
    }

    suspend fun getRaces(season: String = "current"): ErgastResponseDto<RaceTableDataDto> {
        val url = "${baseUrl.trimEnd('/')}/$season.json"
        return client.get(url).body()
    }

    suspend fun getRaceResults(season: String = "current", round: Int): ErgastResponseDto<RaceTableDataDto> {
        val url = "${baseUrl.trimEnd('/')}/$season/$round/results.json"
        return client.get(url).body()
    }

    suspend fun getCircuit(circuitId: String): ErgastResponseDto<CircuitTableDataDto> {
        val url = "${baseUrl.trimEnd('/')}/circuits/$circuitId.json"
        return client.get(url).body()
    }
}