package com.example.Banking_Service.banking

import com.example.Banking_Service.dto.TransactionRequestDTO
import com.example.Banking_Service.dto.TransactionResponseDTO
import com.example.Banking_Service.users.AccountEntity
import com.example.Banking_Service.users.AccountsRepository
import jakarta.inject.Named
import java.math.BigDecimal

@Named
class TransactionsService (
    private val transactionsRepository: TransactionsRepository,
    private val accountsRepository: AccountsRepository

){

    // We have to put validation checks before transferring the funds
    // 1. accounts must exist
    // 2. the amount should be positive
    // 3. there must be sufficient funds in source account
    //
    fun transfer(transferRequest: TransactionRequestDTO): TransactionResponseDTO{
        // 1. find source account
        val sourceAccount = accountsRepository.findByAccountNumber(transferRequest.sourceAccountNumber) as AccountEntity

        // 2. find destination account
        val destinationAccount = accountsRepository.findByAccountNumber(transferRequest.destinationAccountNumber) as AccountEntity

        // check if the funds are positive
        // BigDecimal.ZERO check if account is less or equal to zero
        // if it's true then throw exception
        if(transferRequest.amount <= BigDecimal.ZERO){
            throw IllegalArgumentException("Transfer amount should be bigger than 0")
        }

        // check if account is closed
        if(!sourceAccount.isActive){
            throw IllegalArgumentException("Source account is closed")
        }

        // check if account is closed
        if(!destinationAccount.isActive){
            throw IllegalArgumentException("Destination account is closed")
        }

        // update source and destination account
        // Update balances
        sourceAccount.balance -= transferRequest.amount
        destinationAccount.balance += transferRequest.amount

        // create transaction record
        // since id is nullable we will use !!
        val transaction = TransactionEntity(
            sourceAccount = sourceAccount.id!!,
            destinationAccount = destinationAccount.id!!,
            amount = transferRequest.amount
        )

        transactionsRepository.save(transaction)
        //return the new balance
        return TransactionResponseDTO(newBalance = sourceAccount.balance)
    }
}