package com.example.Banking_Service.users

import jakarta.inject.Named
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import java.math.BigDecimal
import java.time.LocalDate

@Named
interface KycRepository : JpaRepository<KycEntity, Long>

@Entity
@Table(name = "KYC")
data class KycEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val dateOfBirth: LocalDate,
    val nationality: String,
    val salary: BigDecimal,

    // JoinColumn defines the foreign key (JPA annotation)
    @OneToOne // every user have one kyc only bru
    @JoinColumn(name = "user_id")// foreign key for Users(Id)
    val user: UserEntity,
)