package com.illarionova.vika.paymaster.repository

import com.illarionova.vika.paymaster.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Int> {
    fun findUserByUsername(username: String): User?

    fun deleteUserByUsername(username: String): Int

    fun findUserById(id: Int): User?

    fun deleteUserById(id: Int): Int
}