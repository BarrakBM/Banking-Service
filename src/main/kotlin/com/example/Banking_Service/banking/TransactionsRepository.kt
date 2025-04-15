package com.example.Banking_Service.banking

import com.example.Banking_Service.users.AccountEntity
import jakarta.inject.Named
import jakarta.persistence.*
import jdk.jfr.DataAmount
import org.springframework.data.jpa.repository.JpaRepository
import java.math.BigDecimal

@Named
interface TransactionsRepository : JpaRepository<TransactionEntity, Long>

@Entity
@Table(name = "Transactions")

data class TransactionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val sourceAccount: Long,
    val destinationAccount: Long,
    val amount: BigDecimal
) {
    constructor(): this(null, 1, 1, BigDecimal.ZERO)
}