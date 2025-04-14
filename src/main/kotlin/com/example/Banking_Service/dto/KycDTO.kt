package com.example.Banking_Service.dto

import java.math.BigDecimal
import java.time.LocalDate

data class SaveKycDTO(
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: LocalDate,
    val nationality: String?, // not required (nullable)
    val salary: BigDecimal
)

data class KycResponseDTO (
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: LocalDate,
    val salary: BigDecimal,

    )