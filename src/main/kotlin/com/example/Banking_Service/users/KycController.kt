package com.example.Banking_Service.users

import com.example.Banking_Service.dto.KycResponseDTO
import com.example.Banking_Service.dto.SaveKycDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class KycController(
    private val kycRepository: KycRepository,
    private val usersRepository: UsersRepository,
    private val kycService: KycService
){

    // Create or update KYC information
    @PostMapping("/users/v1/kyc")
    fun saveInfoKyc(@RequestBody request: SaveKycDTO): KycResponseDTO {
        return kycService.createOrUpdate(request)
    }

}