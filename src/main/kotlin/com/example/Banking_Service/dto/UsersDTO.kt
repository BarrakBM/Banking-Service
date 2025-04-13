package com.example.Banking_Service.dto

// DTO to register the request
data class RegisterUserDTO(
    val username: String,
    val passkey: String
)

// DTO to register the response
data class UserResponseDTO(
    val id: Long,
    val username: String
)
