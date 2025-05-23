package com.example.Banking_Service.kyc

import com.example.Banking_Service.users.UserEntity
import jakarta.inject.Named
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import java.math.BigDecimal
import java.time.LocalDate

@Named
interface KycRepository : JpaRepository<KycEntity, Long> {
    // method to find KYC by user
    fun findByUser(user: UserEntity?): KycEntity?
}

@Entity
@Table(name = "KYC")
data class KycEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val firstName: String,
    val lastName: String,
    val dateOfBirth: LocalDate,
    val nationality: String?,
    val salary: BigDecimal,

    // JoinColumn defines the foreign key (JPA annotation)
    @OneToOne // every user have one kyc only bru
    @JoinColumn(name = "user_id")// foreign key for Users(Id)
    val user: UserEntity? = null,
){
    constructor() : this(
        id = null,
        firstName = "",
        lastName = "",
        dateOfBirth = LocalDate.now(),
        nationality = null,
        salary = BigDecimal.ZERO,
        user = null
    )}