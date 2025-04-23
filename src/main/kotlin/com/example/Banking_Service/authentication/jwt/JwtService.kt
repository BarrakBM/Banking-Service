package com.example.Banking_Service.authentication.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtService {

    private val secretKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    private val expirationsMs: Long = 1000 * 60 * 60

    // generate token for the user
    fun generateToken(username: String): String {
        val now = Date()
        val expiry = Date(now.time + expirationsMs)

        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(secretKey)
            .compact()
    }

    // extract username from the token itself
    fun extractUsername(token: String): String =
        Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .subject

    // check if token is valid or not
    fun isTokenValid(token: String, username: String): Boolean{
        return try {
            extractUsername(token) == username
        } catch (e: Exception){
            false
        }
    }

}