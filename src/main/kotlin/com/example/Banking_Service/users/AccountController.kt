package com.example.Banking_Service.users

import com.example.Banking_Service.dto.AccountInfoDTO
import com.example.Banking_Service.dto.AccountListResponseDTO
import com.example.Banking_Service.dto.CreateAccountDTO
import com.example.Banking_Service.dto.CreateAccountResponseDTO
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class AccountController(
    private val usersRepository: UsersRepository,
    private val accountService: AccountService
){

    // create account
    @PostMapping("/accounts/v1/accounts")
    fun createAccount(@RequestBody request: CreateAccountDTO): CreateAccountResponseDTO {
        return accountService.createAccount(request)
    }

    // list accounts
    @GetMapping("/accounts/v1/accounts")
    fun getAccounts(): AccountListResponseDTO{
        return accountService.getAccounts()
    }

    // delete account
    @PostMapping("/accounts/v1/accounts/{accountNumber}/close")
    fun closeAccount(@PathVariable accountNumber: String): ResponseEntity<Void> {
        accountService.closeAccount(accountNumber)
        // Return 200 ok
        return ResponseEntity.ok().build()
    }
}