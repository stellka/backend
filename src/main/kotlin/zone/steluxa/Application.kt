package zone.steluxa

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import zone.steluxa.fitures.games.configureGamesRouting
import zone.steluxa.fitures.login.configureLoginRouting
import zone.steluxa.fitures.register.configureRegisterRouting
import zone.steluxa.plugins.*

fun main() {

    val config = HikariConfig("hikari.properties")
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)

    embeddedServer(Netty, port = System.getenv("PORT").toInt()) {
        configureRouting()
        configureSerialization()
        configureLoginRouting()
        configureRegisterRouting()
        configureGamesRouting()
    }.start(wait = true)
}
