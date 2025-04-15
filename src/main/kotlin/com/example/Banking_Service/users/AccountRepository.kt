package com.example.Banking_Service.users

import com.example.Banking_Service.users.UserEntity
import jakarta.inject.Named
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import java.math.BigDecimal

@Named
interface AccountsRepository : JpaRepository<AccountEntity, Long> {
    fun findByAccountNumber(accountNumber: String): Any
}

@Entity
@Table(name = "Account")

data class AccountEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    // Many accounts can have ony user only
    @ManyToOne
    @JoinColumn(name = "user_id") // user_id is the db column
    val user: UserEntity,

    @Column(name="name")
    val name: String,
    var balance: BigDecimal,
    var isActive: Boolean,
    val accountNumber: String
){
    constructor(): this(null,UserEntity(),"", BigDecimal.ZERO,true,"")
}