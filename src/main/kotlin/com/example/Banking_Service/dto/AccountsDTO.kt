package com.example.Banking_Service.dto

import java.math.BigDecimal

data class CreateAccountDTO(
    val userId: Long,
    val name: String,
    val initialBalance: BigDecimal
)

data class CreateAccountResponseDTO(
    val userId: Long,
    val accountNumber: String,
    val name: String,
    val initialBalance: BigDecimal
)

// dto for account list
data class AccountInfoDTO(
    val userId: Long,
    val accountNumber: String,
    val name: String,
    val balance: BigDecimal,
    val isActive: Boolean
)

// putting accounts in a list (wrapping it)
data class AccountListResponseDTO(
    val accounts: List<AccountInfoDTO>
)
