package zone.steluxa.fitures.games.models

import kotlinx.serialization.Serializable


@Serializable
data class FetchGamesRequest(
    val searchQuery: String
)