package com.example.Banking_Service.users


import jakarta.inject.Named
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository

@Named
interface UsersRepository : JpaRepository<UserEntity, Long>

@Entity
@Table(name = "Users")

data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val username: String,
    val passkey: String,

    //This defines a oneToMany relationship (a user can have many accounts)
    // mapped by specify the foreign key in accounts
    // etc... user knows about its accounts and vice versa
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL] )
    val accounts: List<AccountEntity> = emptyList(),

    // oneToOne relationship with KYC
    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL] )
    val kyc: KycEntity? = null,
){
    constructor(): this(null, "", "")
}