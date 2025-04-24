package com.example.Banking_Service

import com.example.Banking_Service.authentication.AuthRequest
import com.example.Banking_Service.authentication.AuthResponse
import com.example.Banking_Service.users.UserEntity
import com.example.Banking_Service.users.UsersRepository
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.assertEquals

@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
	properties = ["kotlin/resources/application-test.properties"]
)
class BankingServiceApplicationTests {
	@Autowired
	lateinit var restTemplate: TestRestTemplate
	//As a user, I can login and get jwt token

	@Autowired
	private lateinit var usersRepository: UsersRepository

	@Autowired
	private lateinit var passwordEncoder: PasswordEncoder

	// companion object can be shared by all instances of the class
	companion object{
		val testUser = "testUser"
		val testPassword = "Test123456"
	}

	// setup
	@BeforeEach
	fun setup(){
		// clear the repo before the test
		usersRepository.deleteAll()

		// create a test user
		val test = UserEntity(
			username = testUser,
			passkey = passwordEncoder.encode(testPassword)
		)
		usersRepository.save(test)
	}

	@Test
	fun `As a user, I can login and get jwt token`(){
		// create a authentication request with test credentials
		val login = AuthRequest(
			username = testUser,
			passkey = testPassword
		)

		// send an http post request to the login endpoint
		val response: ResponseEntity<AuthResponse> = restTemplate.postForEntity(
			"/authentication/login",
			login,
			AuthResponse::class.java
		)

		assertEquals(HttpStatus.OK, response.statusCode) //check status code
		assertNotNull(response.body) //check that response.body is not null
	}

}
