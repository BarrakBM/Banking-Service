package com.example.Banking_Service.dto

import java.math.BigDecimal

data class TransactionRequestDTO(
    val sourceAccountNumber: String,
    val destinationAccountNumber: String,
    val amount: BigDecimal
)

data class TransactionResponseDTO(
    val newBalance: BigDecimal
)