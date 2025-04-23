package com.example.Banking_Service.authentication

import com.example.Banking_Service.users.UserEntity
import com.example.Banking_Service.users.UsersRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class CustomUserDetailsClass(
    private val usersRepository: UsersRepository
): UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user: UserEntity = usersRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User Not Found")

        return User.builder()
            .username(user.username)
            .password(user.passkey)
            //            .roles(user.role.toString())
            .build()
    }
}