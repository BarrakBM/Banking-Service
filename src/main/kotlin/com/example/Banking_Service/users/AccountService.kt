package com.example.Banking_Service.users

import com.example.Banking_Service.dto.AccountInfoDTO
import com.example.Banking_Service.dto.AccountListResponseDTO
import com.example.Banking_Service.dto.CreateAccountDTO
import com.example.Banking_Service.dto.CreateAccountResponseDTO
import java.security.SecureRandom
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
    fun createAccount(dto: CreateAccountDTO): CreateAccountResponseDTO {
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
        return CreateAccountResponseDTO(
            userId = dto.userId,
            accountNumber = newAccountNumber,
            name = dto.name,
            initialBalance = dto.initialBalance

        )
    }

    // 2. get all accounts
    fun getAccounts(): AccountListResponseDTO{
        // get all accounts from the repo
        val accounts = accountsRepository.findAll()

        // get account dto (convert entities to DTO)
        // for each account entity in accounts we're creating a new DTO
        val accountsDTO = accounts.map { account ->
            AccountInfoDTO(
                userId = account.user.id ?: 0,
                accountNumber = account.accountNumber,
                name = account.name,
                balance = account.balance,
                isActive = account.isActive

            )

        }
        return AccountListResponseDTO(accounts = accountsDTO)
    }
}

