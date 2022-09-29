package zone.steluxa.fitures.register

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import zone.steluxa.database.tokens.TokenDto
import zone.steluxa.database.tokens.Tokens
import zone.steluxa.database.users.UserDto
import zone.steluxa.database.users.Users
import zone.steluxa.utils.isValidEmail
import java.util.*

class RegisterController(private val call: ApplicationCall) {

    suspend fun registerNewUser() {
        val registerReceiveRemote = call.receive<RegisterReceiveRemote>()
        if (!registerReceiveRemote.email.isValidEmail()){
            call.respond(HttpStatusCode.BadRequest, "Email is not valid")
        }

        val userDto = Users.fetchUser(registerReceiveRemote.login)

        if (userDto != null) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        } else {
            val token = UUID.randomUUID().toString()
            try {
                Users.insert(
                    UserDto(
                        login = registerReceiveRemote.login,
                        password = registerReceiveRemote.password,
                        email = registerReceiveRemote.email,
                        username = ""
                    )
                )
            }catch (e: ExposedSQLException){
                call.respond(HttpStatusCode.Conflict, "User already exists")
            }
            Tokens.insert(
                TokenDto(
                    rowId = UUID.randomUUID().toString(),
                    login = registerReceiveRemote.login,
                    token=token
                )
            )
            call.respond(RegisterResponseRemote(token = token))
        }
    }
}