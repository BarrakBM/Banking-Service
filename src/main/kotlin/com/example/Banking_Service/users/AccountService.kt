package com.example.Banking_Service.users

import java.security.SecureRandom
import com.example.Banking_Service.dto.createAccountDTO
import com.example.Banking_Service.dto.createAccountResponseDTO
import jakarta.inject.Named

@Named
class AccountService(
    private val usersRepository: UsersRepository,
    private val accountsRepository: AccountsRepository
) {


    // secure random number generator
    private fun generateAccountNumber(): String {

        val secureRandom = SecureRandom()
        // random number of 12 number
        val accountNumber = StringBuilder(12)

        // first digit wont be zero
        accountNumber.append(secureRandom.nextInt(9) + 1)

        // generate the rest of the digit
        for (i in 0 until 11) {
            accountNumber.append(secureRandom.nextInt(10))
        }
        // return as string
        return accountNumber.toString()
    }

    // 1. create Account
    fun createAccount(dto: createAccountDTO): createAccountResponseDTO{
        // check for the user you want to create an account for (find user)
        val user = usersRepository.findById(dto.userId)
            .orElseThrow{NoSuchElementException("user not found")}


        // generate new account number
        val newAccountNumber = generateAccountNumber()

        // create a new account
        val newAccount = AccountEntity(
            user = user,
            name = dto.name,
            balance = dto.initialBalance,
            isActive = true,
            accountNumber = newAccountNumber
        )
        //save the account
        accountsRepository.save(newAccount)

        // return
        return createAccountResponseDTO(
            userId = dto.userId,
            accountNumber = newAccountNumber,
            name = dto.name,
            initialBalance = dto.initialBalance

        )
    }
}

