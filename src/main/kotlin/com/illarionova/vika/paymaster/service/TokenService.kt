package com.illarionova.vika.paymaster.service

import com.illarionova.vika.paymaster.configuration.InvalidTokenException
import com.illarionova.vika.paymaster.configuration.JwtProperties
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Service
class TokenService(
    jwtProperties: JwtProperties
) {

    private val secretKey = Keys.hmacShaKeyFor(
        jwtProperties.key.toByteArray()
    )

    private val blacklistedTokens = ConcurrentHashMap<String, Date>()

    fun generate(
        userDetails: UserDetails,
        expirationDate: Date,
        additionalClaims: Map<String, Any> = emptyMap()
    ) : String =
        Jwts.builder()
            .claims()
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(expirationDate)
            .add(additionalClaims)
            .and()
            .signWith(secretKey)
            .compact()


    fun extractUsername(token: String): String? =
        getAllClaims(token).subject

    fun isExpired(token: String): Boolean =
        getAllClaims(token).expiration.before(Date(System.currentTimeMillis()))

    fun isValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)

        return userDetails.username == username && !isExpired(token) && !isBlacklisted(token)
    }

    fun invalidateToken(token: String) {
        val expirationDate = getAllClaims(token).expiration
        blacklistedTokens[token] = expirationDate
    }

    private fun isBlacklisted(token: String): Boolean {
        return blacklistedTokens.containsKey(token)
    }

    @Scheduled(fixedRate = 3600000) // Every 1 hour
    fun clearExpiredTokens() {
        val now = Date()
        blacklistedTokens.entries.removeIf { it.value.before(now) }
    }

    private fun getAllClaims(token: String): Claims {
        try {
            return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: ExpiredJwtException) {
            throw InvalidTokenException("JWT token is expired")
        } catch (e: UnsupportedJwtException) {
            throw InvalidTokenException("JWT token is unsupported")
        } catch (e: MalformedJwtException) {
            throw InvalidTokenException("JWT token is malformed")
        } catch (e: IllegalArgumentException) {
            throw InvalidTokenException("JWT token is empty")
        }
    }
}