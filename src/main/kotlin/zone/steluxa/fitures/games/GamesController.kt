package zone.steluxa.fitures.games

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import zone.steluxa.database.games.Games
import zone.steluxa.database.games.mapToCreateGameResponse
import zone.steluxa.database.games.mapToGameDTO
import zone.steluxa.fitures.games.models.CreateGameRequest
import zone.steluxa.fitures.games.models.FetchGamesRequest
import zone.steluxa.utils.TokenCheck


class GamesController(private val call: ApplicationCall) {

    suspend fun performSearch() {
        val request = call.receive<FetchGamesRequest>()
        val token = call.request.headers["Bearer-Authorization"]

        if (TokenCheck.isTokenValid(token.orEmpty()) || TokenCheck.isTokenAdmin(token.orEmpty())) {
            if (request.searchQuery.isBlank()) {
                call.respond(Games.fetchAll())
            } else {
                call.respond(Games.fetchAll().filter { it.title.contains(request.searchQuery, ignoreCase = true) })
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }

    suspend fun createGame() {
        val token = call.request.headers["Bearer-Authorization"]
        if (TokenCheck.isTokenAdmin(token.orEmpty())) {
            val request = call.receive<CreateGameRequest>()
            val game = request.mapToGameDTO()
            Games.insert(game)
            call.respond(game.mapToCreateGameResponse())
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }
}