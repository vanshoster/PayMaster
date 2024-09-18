package com.illarionova.vika.paymaster.controller

import com.illarionova.vika.paymaster.entity.User
import com.illarionova.vika.paymaster.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
) {
    @PostMapping
    fun create(@RequestBody user: UserRequest): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(
            userService.createUser(user.toModel())?.toResponse() ?: return ResponseEntity.badRequest().build()
        )
    }
    
    @GetMapping
    fun listAll(): ResponseEntity<List<UserResponse>> {
        return ResponseEntity.ok(
            userService.findAll()
                .map { it.toResponse() }
        )
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(
            userService.findUserById(id)?.toResponse() ?: return ResponseEntity.notFound().build()
        )
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Int): ResponseEntity<Boolean> {
        return if (userService.deleteUserById(id)) {
             ResponseEntity.noContent().build()
        } else {
             ResponseEntity.notFound().build()
        }
    }
}

private fun UserRequest.toModel(): User = User(
    username = this.username,
    password = this.password,
)

private fun User.toResponse(): UserResponse = UserResponse(
    id = this.id,
    username = this.username,
)

data class UserRequest(
    var username: String,
    var password: String
)

data class UserResponse(
    var id: Int,
    var username: String
)
