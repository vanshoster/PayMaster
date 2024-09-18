package com.illarionova.vika.paymaster.configuration

import com.illarionova.vika.paymaster.service.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val userDetailsService: UserDetailsService,
    private val tokenService: TokenService
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val authHeader: String? = request.getHeader("Authorization")

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response)
                return
            }

            val jwtToken = authHeader.extractTokenValue()
            val username = tokenService.extractUsername(jwtToken)

            if (username != null && SecurityContextHolder.getContext().authentication == null) {
                val foundUser = userDetailsService.loadUserByUsername(username)

                if (tokenService.isValid(jwtToken, foundUser)) {
                    updateContext(foundUser, request)
                } else {
                    throw InvalidTokenException("Invalid JWT Token")
                }
            }
            filterChain.doFilter(request, response)
        } catch (ex: InvalidTokenException) {
            handleException(ex, response)
        }
    }


    private fun String.extractTokenValue(): String =
        this.substringAfter("Bearer ")


    private fun updateContext(foundUser: UserDetails, request: HttpServletRequest) {
        val authToken = UsernamePasswordAuthenticationToken(foundUser, null, foundUser.authorities)
        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

        SecurityContextHolder.getContext().authentication = authToken
    }

    private fun handleException(ex: InvalidTokenException, response: HttpServletResponse) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.message)
    }
}

class InvalidTokenException(message: String) : RuntimeException(message)
