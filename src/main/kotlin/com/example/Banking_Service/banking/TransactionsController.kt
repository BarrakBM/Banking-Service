package com.example.Banking_Service.banking

import com.example.Banking_Service.dto.TransactionRequestDTO
import com.example.Banking_Service.dto.TransactionResponseDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class TransactionController(
    private val transactionsService: TransactionsService
) {

    // transfer funds
    @PostMapping("/accounts/v1/accounts/transfer")
    fun transferFunds(@RequestBody request: TransactionRequestDTO): Any {
//        return transactionsService.transfer(request)
        try {
            return transactionsService.transfer(request)
        }catch (e: IllegalArgumentException){
            return ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }
}

