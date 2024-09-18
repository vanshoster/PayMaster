package com.illarionova.vika.paymaster.entity

import jakarta.persistence.*

@Entity
@Table(name = "users", schema = "payroll")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @Column
    val username: String = "",
    @Column
    val password: String = "",

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    val role: Role = Role(1, "USER")
)

@Entity
@Table(name = "roles", schema = "payroll")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(nullable = false)
    val name: String = ""
)
