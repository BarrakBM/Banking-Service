package com.example.Banking_Service

import com.example.Banking_Service.authentication.AuthRequest
import com.example.Banking_Service.authentication.AuthResponse
import com.example.Banking_Service.dto.*
import com.example.Banking_Service.kyc.KycRepository
import com.example.Banking_Service.users.UserEntity
import com.example.Banking_Service.users.UsersRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertFalse

@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
	properties = ["kotlin/resources/application-test.properties"]
)
class BankingServiceApplicationTests {
	@Autowired
	lateinit var restTemplate: TestRestTemplate
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
			name = "Debit Account",
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
			name = "Investment Account",
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

	//4th test
	@Test
	fun `As a developer, I can test reading a list of accounts `(){

		//get authentication token
		val token = getAuthToken()

		//get user id
		val user = usersRepository.findByUsername(testUser)

		// create first account request
		val account1 = CreateAccountDTO(
			userId = user?.id!!,
			name = "Debit Account",
			initialBalance = BigDecimal("4000.00")
		)
		//create 2nd account
		val account2 = CreateAccountDTO(
			userId = user.id!!,
			name = "Saving Account",
			initialBalance = BigDecimal("5000.00")
		)

		val response1: ResponseEntity<CreateAccountResponseDTO> = restTemplate.postForEntity(
			"/accounts/v1/accounts",
			authRequest(account1, token),
			CreateAccountResponseDTO::class.java
		)
		val response2: ResponseEntity<CreateAccountResponseDTO> = restTemplate.postForEntity(
			"/accounts/v1/accounts",
			authRequest(account2, token),
			CreateAccountResponseDTO::class.java
		)

		// create authenticated get request
		val headers = HttpHeaders()
		headers.setBearerAuth(token)
		val getRequest = HttpEntity<Any>(headers)

		// retrieve list of accounts
		val response: ResponseEntity<AccountListResponseDTO> = restTemplate.exchange(
			"/accounts/v1/accounts",
			HttpMethod.GET,
			getRequest,
			AccountListResponseDTO::class.java
		)

		assertEquals(HttpStatus.OK, response.statusCode) // check status code
		assertEquals(2, response.body?.accounts?.size)// making sure there's 2 accounts in the list

		val accounts = response.body?.accounts
		assertNotNull(accounts) // checking it's not null
		if (accounts != null) {
			assertTrue(accounts.any { it.name == "Debit Account" })
		}
		if (accounts != null) {
			assertTrue(accounts.any { it.name == "Saving Account" })
		}
	}

	// 5th test
	@Test
	fun `As a develop, I can test user create profile and update it`(){
		//get authentication token
		val token = getAuthToken()

		//get user id
		val user = usersRepository.findByUsername(testUser)

		// create a profile
		val createKyc = SaveKycDTO(
			userId = user?.id!!,
			firstName = "Walter",
			lastName = "White",
			nationality = "Kuwait",
			dateOfBirth = LocalDate.of(1960,1,1),
			salary = BigDecimal(100000.00)
		)

		val response: ResponseEntity<KycResponseDTO> = restTemplate.postForEntity(
			"/users/v1/kyc",
			authRequest(createKyc, token),
			KycResponseDTO::class.java
		)

		//verify response
		assertEquals(HttpStatus.OK, response.statusCode)

		// update a profile
		val updateKyc = SaveKycDTO(
			userId = user.id!!,
			firstName = "Jessi",
			lastName = "White",
			nationality = "USA",
			dateOfBirth = LocalDate.of(1960,1,1),
			salary = BigDecimal(4000)
		)

		val response2: ResponseEntity<KycResponseDTO> = restTemplate.postForEntity(
			"/users/v1/kyc",
			authRequest(updateKyc, token),
			KycResponseDTO::class.java
		)

		//verify response
		assertEquals(HttpStatus.OK, response2.statusCode)
		assertEquals("Jessi", response2.body?.firstName)
		assertEquals(BigDecimal("4000"), response2.body?.salary)

	}

	//6th test
	@Test
	fun `As a develop, I can test user read profile`(){
		//get authentication token
		val token = getAuthToken()

		//get user id
		val user = usersRepository.findByUsername(testUser)

			// create a profile
		val createKyc = SaveKycDTO(
			userId = user?.id!!,
			firstName = "Walter",
			lastName = "White",
			nationality = "Kuwait",
			dateOfBirth = LocalDate.of(1960,1,1),
			salary = BigDecimal(100000.00)
		)


		// create a kyc
		val response: ResponseEntity<KycResponseDTO> = restTemplate.postForEntity(
			"/users/v1/kyc",
			authRequest(createKyc, token),
			KycResponseDTO::class.java
		)
		//verify response
		assertEquals(HttpStatus.OK, response.statusCode)

		// create authenticated get request
		val headers = HttpHeaders()
		headers.setBearerAuth(token)
		val getRequest = HttpEntity<Any>(headers)

		// read kyc information

		val getResponse: ResponseEntity<KycResponseDTO> = restTemplate.exchange(
			"/users/v1/kyc/${user.id}",
			HttpMethod.GET,
			getRequest,
			KycResponseDTO::class.java
		)

		// verify response
		// and check the values are correct
		assertEquals(HttpStatus.OK, getResponse.statusCode) // return code
		assertNotNull(getResponse.body)
		assertEquals("Walter", getResponse.body?.firstName)
		assertEquals("White", getResponse.body?.lastName)
		assertEquals(LocalDate.of(1960, 1, 1), getResponse.body?.dateOfBirth)
		assertEquals(BigDecimal("100000.00"), getResponse.body?.salary)
		assertEquals(user.id, getResponse.body?.userId)
	}

	// closing a user account
	@Test
	fun `As a developer, I can test closing a user account`(){

		//get authentication token
		val token = getAuthToken()

		//get user id
		val user = usersRepository.findByUsername(testUser)

		//create account
		val account1 = CreateAccountDTO(
			userId = user?.id!!,
			name = testUser,
			initialBalance = BigDecimal(1000.00)
		)

		// creating first account
		val response: ResponseEntity<CreateAccountResponseDTO> = restTemplate.postForEntity(
			"/accounts/v1/accounts",
			authRequest(account1, token),
			CreateAccountResponseDTO::class.java
		)
		assertEquals(HttpStatus.OK, response.statusCode) // check status code

		// getting the default account number from the response
		val accountNumber = response.body?.accountNumber

		//now closing the account
		val headers = HttpHeaders()
		headers.setBearerAuth(token)
		val closeRequest = HttpEntity<Any>(headers)

		val closeResponse: ResponseEntity<Any> = restTemplate.postForEntity(
			"/accounts/v1/accounts/$accountNumber/close",
			closeRequest,
			Any::class.java
		)
		// verify account is closed (set to false)
		assertEquals(HttpStatus.OK, response.statusCode) // check status code

	}

	@Test
	fun `As a developer, I can test transferring money to anther account`(){
		//get authentication token
		val token = getAuthToken()

		//get user id
		val user = usersRepository.findByUsername(testUser)

		// create first account request
		val account1 = CreateAccountDTO(
			userId = user?.id!!,
			name = "Debit Account",
			initialBalance = BigDecimal("4000.00")
		)
		//create 2nd account
		val account2 = CreateAccountDTO(
			userId = user.id!!,
			name = "Saving Account",
			initialBalance = BigDecimal("10000.00")
		)

		//response for account 1
		val response1: ResponseEntity<CreateAccountResponseDTO> = restTemplate.postForEntity(
			"/accounts/v1/accounts",
			authRequest(account1, token),
			CreateAccountResponseDTO::class.java
		)
		assertEquals(HttpStatus.OK, response1.statusCode) // check status code
		// getting the default account number from the response
		val accountNumber1 = response1.body?.accountNumber

		//account2 response
		val response2: ResponseEntity<CreateAccountResponseDTO> = restTemplate.postForEntity(
			"/accounts/v1/accounts",
			authRequest(account2, token),
			CreateAccountResponseDTO::class.java
		)
		assertEquals(HttpStatus.OK, response2.statusCode) // check status code

		// getting the default account number from the response
		val accountNumber2 = response2.body?.accountNumber

		// creating a transfer request
		val transferRequest = TransactionRequestDTO(
			sourceAccountNumber = accountNumber1!!,
			destinationAccountNumber = accountNumber2!!,
			amount = BigDecimal("1000.00")
		)

		// creating a transfer response
		val transferResponse: ResponseEntity<TransactionResponseDTO> = restTemplate.postForEntity(
			"/accounts/v1/accounts/transfer",
			authRequest(transferRequest, token),
			TransactionResponseDTO::class.java
		)
		assertEquals(HttpStatus.OK, transferResponse.statusCode) // check status code


		val headers = HttpHeaders()
		headers.setBearerAuth(token)
		val getRequest = HttpEntity<Any>(headers)
		// getting the accounts in a list
		val accountsResponse: ResponseEntity<AccountListResponseDTO> = restTemplate.exchange(
			"/accounts/v1/accounts",
			HttpMethod.GET,
			getRequest,
			AccountListResponseDTO::class.java
		)

		val accounts = accountsResponse.body?.accounts //list of accounts

		// getting the accounts numbers
		val sourceAccount = accounts?.find {it.accountNumber == accountNumber1}
		val destinationAccount = accounts?.find {it.accountNumber == accountNumber2}

		// checking the values
		assertEquals(BigDecimal("3000.00"), sourceAccount?.balance)
		assertEquals(BigDecimal("11000.00"), destinationAccount?.balance)
	}
}
