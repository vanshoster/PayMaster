package com.illarionova.vika.paymaster.service

import com.illarionova.vika.paymaster.configuration.JwtProperties
import com.illarionova.vika.paymaster.controller.AuthenticationRequest
import com.illarionova.vika.paymaster.controller.AuthenticationResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticationService(
    private val authManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties
) {
    fun authenticate(authRequest: AuthenticationRequest): AuthenticationResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authRequest.username,
                authRequest.password
            )
        )
        val user = userDetailsService.loadUserByUsername(authRequest.username)
        val accessToken = tokenService.generate(user,Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration))

        return AuthenticationResponse(accessToken)
    }
}
