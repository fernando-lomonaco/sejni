package br.com.lomonaco.sejni.service

import br.com.lomonaco.sejni.dto.UserDTO
import br.com.lomonaco.sejni.model.User
import java.util.*

interface UserService {
    fun create(userDTO: UserDTO): UserDTO
    fun getEmail(): String?
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun findByUsername(username: String?): Optional<User>
}