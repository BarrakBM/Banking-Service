package com.example.Banking_Service.users

import com.example.Banking_Service.dto.RegisterUserDTO
import com.example.Banking_Service.dto.UserResponseDTO
import jakarta.inject.Named

const val MAX_CHAR = 15
const val MIN_CHAR = 6
const val MIN_PASS_CHAR = 7

@Named
class UsersService(
    private val usersRepository: UsersRepository
) {

    // registering a user
    fun registerUser(request: RegisterUserDTO): UserResponseDTO {

        if(request.username.length < MIN_CHAR){
            throw IllegalArgumentException("username characters should be 6 at least")
        }

        if(request.username.length > MAX_CHAR){
            throw IllegalArgumentException("characters shouldn't exceed 15")
        }
        if(request.passkey.length < MIN_PASS_CHAR){
            throw IllegalArgumentException("password should be 7 characters at least")
        }



        val newUser = UserEntity(
            username = request.username,
            passkey = request.passkey
        )



        val savedUser =  usersRepository.save(newUser) // return new user
        // return a response with user id and username
        return UserResponseDTO(id = savedUser.id!!, username = savedUser.username)
    }

}
