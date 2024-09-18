package com.illarionova.vika.paymaster.controller

import com.illarionova.vika.paymaster.service.AuthenticationService
import com.illarionova.vika.paymaster.service.TokenService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationService: AuthenticationService,
    private val tokenService: TokenService
) {

    @PostMapping("/login")
    fun authenticate(@RequestBody authRequest: AuthenticationRequest): AuthenticationResponse =
        authenticationService.authenticate(authRequest)

    @PostMapping("/logout")
    fun logout(@RequestHeader("Authorization") authHeader: String): ResponseEntity<Void> {
        val token = authHeader.substringAfter("Bearer ")
        tokenService.invalidateToken(token)
        return ResponseEntity.noContent().build()
    }
}

data class AuthenticationResponse(
    val token: String,
)

data class AuthenticationRequest(
    val username: String,
    val password: String
)
