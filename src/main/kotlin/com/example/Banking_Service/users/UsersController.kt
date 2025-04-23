package com.example.Banking_Service.users

import com.example.Banking_Service.dto.RegisterUserDTO
import com.example.Banking_Service.dto.UserResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class UsersController(
    private val usersRepository: UsersRepository,
    private val usersService: UsersService,

) {


    // register users
    @PostMapping("/users/v1/register")
    fun register(@RequestBody request: RegisterUserDTO): Any {
        try {
            return usersService.registerUser(request)
        } catch (e: IllegalArgumentException) {
            // Only catch the specific IllegalArgumentException (for MAX_CHAR validation)
            return ResponseEntity.badRequest().body(mapOf("error" to e.message))

        }


    }

}

data class RegisterUserRequest(
    var username: String,
    var passkey: String
)