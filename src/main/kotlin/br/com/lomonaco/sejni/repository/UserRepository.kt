package br.com.lomonaco.sejni.repository

import br.com.lomonaco.sejni.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String?): User?
    fun existsByName(name: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun findByName(username: String?): Optional<User>

}
