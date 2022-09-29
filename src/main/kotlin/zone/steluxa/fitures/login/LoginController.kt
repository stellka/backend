package zone.steluxa.fitures.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import zone.steluxa.cache.InMemoryCache
import zone.steluxa.cache.TokenCache
import zone.steluxa.database.tokens.TokenDto
import zone.steluxa.database.tokens.Tokens
import zone.steluxa.database.users.Users
import java.util.*

class LoginController(private val call:ApplicationCall) {
    suspend fun performLogin(){
        val receive = call.receive<LoginReceiveRemote>()
        val userDto = Users.fetchUser(receive.login)
        println("receive -> $receive, dto -> $userDto")

        if (userDto == null){
            call.respond(HttpStatusCode.BadRequest, "User not found")
        }else{
            if (userDto.password == receive.password){
                val token = UUID.randomUUID().toString()
                Tokens.insert(
                    TokenDto(
                        rowId = UUID.randomUUID().toString(),
                        login = receive.login,
                        token=token
                    )
                )
                call.respond(LoginResponseRemote(token = token))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid password")
            }
        }
    }
}