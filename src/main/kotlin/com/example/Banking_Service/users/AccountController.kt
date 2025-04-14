package com.example.Banking_Service.users

import com.example.Banking_Service.dto.createAccountDTO
import com.example.Banking_Service.dto.createAccountResponseDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class AccountController(
    private val usersRepository: UsersRepository,
    private val AccountService: AccountService
){

    // create account
    @PostMapping("/accounts/v1/accounts")
    fun createAccount(@RequestBody request: createAccountDTO): createAccountResponseDTO{
        return AccountService.createAccount(request)
    }


}