package com.example.Banking_Service.users

import com.example.Banking_Service.users.UserEntity
import jakarta.inject.Named
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import java.math.BigDecimal

@Named
interface AccountsRepository : JpaRepository<UserEntity, Long>

@Entity
@Table(name = "Accounts")

data class AccountEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    val id: Long? = null,
    val userId: Long?,
    val balance: BigDecimal,
    val isActive: Boolean,
    val accountNumber: String
)