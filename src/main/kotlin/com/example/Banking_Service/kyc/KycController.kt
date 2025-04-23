package com.example.Banking_Service.kyc

import com.example.Banking_Service.dto.KycResponseDTO
import com.example.Banking_Service.dto.SaveKycDTO
import com.example.Banking_Service.users.UsersRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class KycController(
    private val kycRepository: KycRepository,
    private val usersRepository: UsersRepository,
    private val kycService: KycService
){

    // Create or update KYC information
    @PostMapping("/users/v1/kyc")
    fun saveInfoKyc(@RequestBody request: SaveKycDTO): Any {
        try{
            return kycService.createOrUpdate(request)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    // get Kyc Info
    //@pathvariable will capture the {userId}
    @GetMapping("/users/v1/kyc/{userId}")
    fun getKycInfo(@PathVariable userId: Long): KycResponseDTO{
        return kycService.getKycInfo(userId)
    }

}