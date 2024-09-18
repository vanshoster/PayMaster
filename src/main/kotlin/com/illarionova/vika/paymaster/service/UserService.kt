package com.illarionova.vika.paymaster.service

import com.illarionova.vika.paymaster.entity.User
import com.illarionova.vika.paymaster.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder
) {
    @Transactional
    fun createUser(user: User) : User? {
        val existingUser = userRepository.findUserByUsername(user.username)

        return if (existingUser == null) {
            val updated = user.copy(password = encoder.encode(user.password))
            userRepository.save(updated)
        } else {
            null
        }
    }

    fun findByUsername(username: String): User? {
        return userRepository.findUserByUsername(username)
    }

    fun findUserById(id: Int): User? {
        return userRepository.findUserById(id)
    }

    @Transactional
    fun deleteByUsername(username: String) : Boolean {
        if (userRepository.findUserByUsername(username)?.role?.name == "ADMIN") return false
        return userRepository.deleteUserByUsername(username) > 0
    }

    @Transactional
    fun deleteUserById(id: Int) : Boolean {
        if (userRepository.findUserById(id)?.role?.name == "ADMIN") return false
        return userRepository.deleteUserById(id) > 0
    }

    fun findAll(): List<User> {
        return userRepository.findAll()
    }
}
