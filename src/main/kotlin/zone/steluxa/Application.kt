package zone.steluxa

import io.ktor.server.engine.*
import io.ktor.server.cio.*
import org.jetbrains.exposed.sql.Database
import zone.steluxa.fitures.games.configureGamesRouting
import zone.steluxa.fitures.login.configureLoginRouting
import zone.steluxa.fitures.register.configureRegisterRouting
import zone.steluxa.plugins.*

fun main() {
    Database.connect("jdbc:postgresql://localhost:5432/zone", driver = "org.postgresql.Driver", user = "postgres", password = "ssd15122001")
    embeddedServer(CIO, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
        configureLoginRouting()
        configureRegisterRouting()
        configureGamesRouting()
    }.start(wait = true)
}
