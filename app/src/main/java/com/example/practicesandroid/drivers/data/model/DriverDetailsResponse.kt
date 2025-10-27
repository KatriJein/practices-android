package com.example.practicesandroid.drivers.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*

@Keep
@Serializable
class DriverDetailsResponse(
    val driver: DriverWithId?,
    val team: Team?,
    val results: List<DriverResult>?
)

@Keep
@Serializable
data class DriverWithId(
    val driverId: String,
    val name: String,
    val surname: String,
    val nationality: String,
    val birthday: String,
    val number: Int,
    val shortName: String,
    val url: String
)

@Keep
@Serializable
class DriverResult(
    val race: Race?,
    val result: Result?,
    val sprintResult: Result?
)

@Keep
@Serializable
class Race(
    val raceId: String?,
    val name: String?,
    val round: Int?,
    val date: String?,
    val circuit: Circuit?,
)

@Keep
@Serializable
class Circuit(
    val circuitId: String?,
    val name: String?,
    val country: String?,
    val city: String?,
    val length: Int?,
    val lapRecord: String?,
    val firstParticipationYear: Int?,
    val numberOfCorners: Int?,
    val fastestLapYear: Int?,
    val fastestLapDriverId: String?,
    val fastestLapTeamId: String?,
)

@Keep
@Serializable
class Result(
    @Serializable(with = StringOrIntSerializer::class)
    val finishingPosition: String?,
    val gridPosition: Int?,
    val raceTime: String?,
    val pointsObtained: Int?,
    val retired: Boolean?,
)

object StringOrIntSerializer : JsonTransformingSerializer<String?>(String.serializer().nullable) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return when {
            element is JsonPrimitive && !element.isString -> {
                JsonPrimitive(element.content)
            }
            else -> element
        }
    }
}
