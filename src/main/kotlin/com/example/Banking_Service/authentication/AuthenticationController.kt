package com.example.Banking_Service.authentication

import com.example.Banking_Service.authentication.jwt.JwtService
import org.springframework.security.authentication.*
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/authentication")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
    private val jwtService: JwtService
) {

    // login page
    @PostMapping("/login")
    fun login(@RequestBody authRequest: AuthRequest): AuthResponse {

        // create a spring security authentication using the username and password from the request
        val authToken = UsernamePasswordAuthenticationToken(authRequest.username, authRequest.passkey)
        // then the credentials will be authenticated by authentication manager
        val authentication = authenticationManager.authenticate(authToken)

        // check if authentication is successful
        if (authentication.isAuthenticated) {

            // load the user details from the userDetailsService
            val userDetails = userDetailsService.loadUserByUsername(authRequest.username)
            // Uses a JWT (JSON Web Token) service to generate a token for the authenticated user.
            val token = jwtService.generateToken(userDetails.username)
            // Returns a response object containing the JWT token
            return AuthResponse(token)
        } else {
            throw UsernameNotFoundException("Invalid user request!")
        }
    }
}

data class AuthRequest(
    val username: String,
    val passkey: String
)

data class AuthResponse(
    val token: String
)
