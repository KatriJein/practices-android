package com.example.practicesandroid.drivers.data.mapper

import com.example.practicesandroid.core.tryParseServerDate
import com.example.practicesandroid.drivers.data.model.Circuit
import com.example.practicesandroid.drivers.data.model.DriverDetailsResponse
import com.example.practicesandroid.drivers.data.model.DriverResult
import com.example.practicesandroid.drivers.data.model.DriversListResponse
import com.example.practicesandroid.drivers.data.model.Race
import com.example.practicesandroid.drivers.data.model.Result
import com.example.practicesandroid.drivers.data.model.Team
import com.example.practicesandroid.drivers.domain.model.CircuitEntity
import com.example.practicesandroid.drivers.domain.model.DriverDetailsEntity
import com.example.practicesandroid.drivers.domain.model.DriverEntity
import com.example.practicesandroid.drivers.domain.model.RaceEntity
import com.example.practicesandroid.drivers.domain.model.RaceResultEntity
import com.example.practicesandroid.drivers.domain.model.ResultEntity
import com.example.practicesandroid.drivers.domain.model.TeamEntity
import java.time.LocalDateTime

class DriversResponseToEntityMapper {
    fun mapResponse(response: DriversListResponse): List<DriverEntity> {
        return response.drivers_championship?.map { driver ->
            DriverEntity(
                id = driver.driverId,
                name = driver.driver?.name,
                surname = driver.driver?.surname,
                nationality = driver.driver?.nationality,
                birthday = tryParseServerDate(driver.driver?.birthday),
                number = driver.driver?.number,
                points = driver.points,
                position = driver.position,
                wins = driver.wins,
                team = TeamEntity(
                    id = driver.team?.teamId,
                    name = driver.team?.teamName,
                    nationality = driver.team?.teamNationality,
                    year = driver.team?.firstAppeareance
                )

            )
        }.orEmpty()
    }

    fun mapDriverDetailsResponse(response: DriverDetailsResponse): DriverDetailsEntity {
        return DriverDetailsEntity(
            driver = mapDriver(response),
            results = mapResults(response.results ?: emptyList())
        )
    }

    private fun mapDriver(response: DriverDetailsResponse): DriverEntity? {
        val driver = response.driver ?: return null
        val team = response.team ?: return null

        return DriverEntity(
            id = response.driver.driverId,
            name = response.driver.name,
            surname = response.driver.surname,
            nationality = response.driver.nationality,
            birthday = tryParseServerDate(response.driver.birthday),
            number = response.driver.number,
            team = mapTeam(response.team),
            points = null,
            position = null,
            wins = null
        )
    }

    private fun mapTeam(team: Team): TeamEntity? {
        return if (team.teamId != null && team.teamName != null) {
            TeamEntity(
                id = team.teamId,
                name = team.teamName,
                nationality = team.teamNationality,
                year = team.firstAppeareance
            )
        } else {
            null
        }
    }

    private fun mapResults(results: List<DriverResult>): List<RaceResultEntity> {
        return results.mapNotNull { result ->
            val race = result.race ?: return@mapNotNull null
            val mappedRace = mapRace(race) ?: return@mapNotNull null

            RaceResultEntity(
                race = mappedRace,
                result = result.result?.let { mapResult(it) },
                sprintResult = result.sprintResult?.let { mapResult(it) }
            )
        }
    }

    private fun mapRace(race: Race): RaceEntity? {
        return if (race.raceId != null && race.name != null) {
            val circuit = race.circuit
            val mappedCircuit = circuit?.let { mapCircuit(it) } ?: return null

            RaceEntity(
                id = race.raceId,
                name = race.name,
                round = race.round ?: 0,
                date = tryParseServerDate(race.date) ?: LocalDateTime.now(),
                circuit = mappedCircuit
            )
        } else {
            null
        }
    }

    private fun mapCircuit(circuit: Circuit): CircuitEntity? {
        return if (circuit.circuitId != null && circuit.name != null) {
            CircuitEntity(
                id = circuit.circuitId,
                name = circuit.name,
                country = circuit.country ?: "",
                city = circuit.city ?: "",
                length = circuit.length ?: 0,
                lapRecord = circuit.lapRecord ?: "",
                firstParticipationYear = circuit.firstParticipationYear ?: 0,
                numberOfCorners = circuit.numberOfCorners ?: 0,
                fastestLapYear = circuit.fastestLapYear,
                fastestLapDriverId = circuit.fastestLapDriverId,
                fastestLapTeamId = circuit.fastestLapTeamId
            )
        } else {
            null
        }
    }

    private fun mapResult(result: Result): ResultEntity? {
        return ResultEntity(
            finishingPosition = result.finishingPosition,
            gridPosition = result.gridPosition,
            raceTime = result.raceTime,
            pointsObtained = result.pointsObtained,
            retired = result.retired
        )
    }
}