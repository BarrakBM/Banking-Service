package com.example.Banking_Service

import com.example.Banking_Service.authentication.AuthRequest
import com.example.Banking_Service.authentication.AuthResponse
import com.example.Banking_Service.dto.CreateAccountDTO
import com.example.Banking_Service.dto.CreateAccountResponseDTO
import com.example.Banking_Service.dto.RegisterUserDTO
import com.example.Banking_Service.dto.UserResponseDTO
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
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import java.math.BigDecimal
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

	// a helper function to get jwt token
	fun getAuthToken(): String {
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

		return response.body?.token ?: throw AssertionError("Token is null")

	}

	// function to create the authentication request
	fun <T> authRequest(body: T, token: String): HttpEntity<T>{
		val headers = HttpHeaders()
		headers.setBearerAuth(token)
		return HttpEntity(body, headers)
	}

	// 1st test
	@Test
	fun `As a user, I can login and get jwt token`(){
		// create a authentication request with test credentials
		val login = AuthRequest(
			username = testUser,
			passkey = testPassword
		)

		// send a http post request to the login endpoint
		val response: ResponseEntity<AuthResponse> = restTemplate.postForEntity(
			"/authentication/login",
			login,
			AuthResponse::class.java
		)

		assertEquals(HttpStatus.OK, response.statusCode) //check status code
		assertNotNull(response.body) //check that response.body is not null
	}

	// 2nd test
	@Test
	fun `As a developer, I can test user regastration endpoint`(){
		// create a regastration request
		val regastration = RegisterUserDTO(
			username = testUser,
			passkey = testPassword
		)

		// send http post request to the regastration endpoint

		val response: ResponseEntity<UserResponseDTO> = restTemplate.postForEntity(
			"/users/v1/register",
			regastration,
			UserResponseDTO::class.java
		)

		assertEquals(HttpStatus.OK, response.statusCode) //check status code
		assertEquals(testUser, response.body?.username) // check if username matches

	}

	//3rd test
	@Test
	fun `As a developer, I can test multiple account creation`(){

		//get authentication token
		val token = getAuthToken()

		//get user id
		val user = usersRepository.findByUsername(testUser)

		// create first account request
		val account1 = CreateAccountDTO(
			userId = user?.id!!,
			name = "Debit card",
			initialBalance = BigDecimal("2000.00")
		)

		// send post request for 1st account
		val response1: ResponseEntity<CreateAccountResponseDTO> = restTemplate.postForEntity(
			"/accounts/v1/accounts",
			authRequest(account1, token),
			CreateAccountResponseDTO::class.java
		)
		assertEquals(HttpStatus.OK, response1.statusCode) // check status code

		// create 2nd Account
		val account2 = CreateAccountDTO(
			userId = user.id!!,
			name = "Debit card",
			initialBalance = BigDecimal("2000.00")
		)

		// send post request for 1st account
		val response2: ResponseEntity<CreateAccountResponseDTO> = restTemplate.postForEntity(
			"/accounts/v1/accounts",
			authRequest(account2, token),
			CreateAccountResponseDTO::class.java
		)
		assertEquals(HttpStatus.OK, response2.statusCode) // check status code
	}

}
