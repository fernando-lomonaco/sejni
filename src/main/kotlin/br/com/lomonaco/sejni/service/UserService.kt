package br.com.lomonaco.sejni.service

import br.com.lomonaco.sejni.model.User
import java.util.*

interface UserService {
    fun create(user: User): User
    fun getEmail(): String?
    fun existsByName(name: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun findByUsername(username: String?): Optional<User>
}