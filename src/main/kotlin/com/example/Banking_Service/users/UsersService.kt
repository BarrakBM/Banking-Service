package com.example.Banking_Service.users

import com.example.Banking_Service.dto.RegisterUserDTO
import com.example.Banking_Service.dto.UserResponseDTO
import jakarta.inject.Named

@Named
class UsersService(
    private val usersRepository: UsersRepository
) {

    // registering a user
    fun registerUser(request: RegisterUserDTO): UserResponseDTO {
        val newUser = UserEntity(
            username = request.username,
            passkey = request.passkey
        )

        val savedUser =  usersRepository.save(newUser) // return new user
        // return a response with user id and username
        return UserResponseDTO(id = savedUser.id!!, username = savedUser.username)
    }

}
