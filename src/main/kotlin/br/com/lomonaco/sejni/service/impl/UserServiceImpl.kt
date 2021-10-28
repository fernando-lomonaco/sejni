package br.com.lomonaco.sejni.service.impl

import br.com.lomonaco.sejni.model.User
import br.com.lomonaco.sejni.repository.UserRepository
import br.com.lomonaco.sejni.service.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl(
    private val repository: UserRepository, private val bCryptPasswordEncoder: BCryptPasswordEncoder
) : UserService {

    override fun create(user: User): User {
        user.password = bCryptPasswordEncoder.encode(user.password)
        return repository.save(user)
    }

    override fun getEmail(): String? {
        return repository.findByEmail(getCurrentUserEmail())?.name
    }

    override fun existsByName(name: String): Boolean = repository.existsByName(name)

    override fun existsByEmail(email: String): Boolean = repository.existsByEmail(email)

    override fun findByUsername(username: String?): Optional<User> = repository.findByName(username)

    private fun getCurrentUserEmail(): String {
        val user = SecurityContextHolder.getContext().authentication.principal as UserDetailsImpl
        return user.username
    }
}