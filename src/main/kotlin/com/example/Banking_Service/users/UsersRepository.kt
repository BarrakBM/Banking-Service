package com.example.Banking_Service.users


import jakarta.inject.Named
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository

@Named
interface UsersRepository : JpaRepository<UserEntity, Long>

@Entity
@Table(name = "Users")

data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val username: String,
    val passkey: String
){
    constructor(): this(null, "", "")
}