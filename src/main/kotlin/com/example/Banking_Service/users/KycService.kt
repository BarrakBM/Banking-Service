package com.example.Banking_Service.users

import com.example.Banking_Service.dto.KycResponseDTO
import com.example.Banking_Service.dto.SaveKycDTO
import jakarta.inject.Named

@Named
class KycService(
    private val kycRepository: KycRepository,
    private val usersRepository: UsersRepository
) {

    fun createOrUpdate(dto: SaveKycDTO): KycResponseDTO{
        // check if user already exist (find user)
        val user = usersRepository.findById(dto.userId)
            .orElseThrow{NoSuchElementException("user not found")}

        // check if kyc exists
        val kycRecord = kycRepository.findByUser(user)

        val kyc = if(kycRecord != null){
            // if it's not null it will create a new kyc entity, id will be the same but updated valued
            val updatedKyc = kycRecord.copy(
                firstName = dto.firstName,
                lastName = dto.lastName,
                dateOfBirth = dto.dateOfBirth,
                nationality = dto.nationality,
                salary = dto.salary
            )
            kycRepository.save(updatedKyc)
        } else {
            // if kyc is null, then create a new Kyc
            val newKyc = KycEntity(
                firstName = dto.firstName,
                lastName = dto.lastName,
                dateOfBirth = dto.dateOfBirth,
                nationality = dto.nationality,
                salary = dto.salary,
                user = user
            )
            kycRepository.save(newKyc)
        }

        // Return response DTO
        return KycResponseDTO(
            userId = dto.userId,
            firstName = dto.firstName,
            lastName = dto.lastName,
            dateOfBirth = dto.dateOfBirth,
            salary = dto.salary

        )
    }
}