package com.illarionova.vika.paymaster.service

import com.illarionova.vika.paymaster.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

typealias ApplicationUser = com.illarionova.vika.paymaster.entity.User

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.findUserByUsername(username)?.mapToUserDetails()?: throw UsernameNotFoundException("User not found: $username")



    private fun ApplicationUser.mapToUserDetails(): UserDetails =
        User.builder()
            .username(this.username)
            .password(this.password)
            .roles(this.role.name)
            .build()

}
