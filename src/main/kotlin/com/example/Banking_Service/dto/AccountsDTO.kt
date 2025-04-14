package com.example.Banking_Service.dto

import java.math.BigDecimal

data class createAccountDTO(
    val userId: Long,
    val name: String,
    val initialBalance: BigDecimal
)

data class createAccountResponseDTO(
    val userId: Long,
    val accountNumber: String,
    val name: String,
    val initialBalance: BigDecimal
)
