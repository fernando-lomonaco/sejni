package br.com.lomonaco.sejni.service.impl

import br.com.lomonaco.sejni.dto.UserDTO
import br.com.lomonaco.sejni.mapper.UserMapper
import br.com.lomonaco.sejni.model.User
import br.com.lomonaco.sejni.model.security.JwtUser
import br.com.lomonaco.sejni.repository.UserRepository
import br.com.lomonaco.sejni.service.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl(
    private val repository: UserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val userMapper: UserMapper
) : UserService {

    override fun create(userDTO: UserDTO): UserDTO {
        userDTO.password = bCryptPasswordEncoder.encode(userDTO.password)
        val user = repository.save(userMapper.toEntity(userDTO))
        return userMapper.fromEntity(user)
    }

    override fun getEmail(): String? {
        return repository.findByEmail(getCurrentUserEmail())?.username
    }

    override fun existsByUsername(username: String): Boolean = repository.existsByUsername(username)

    override fun existsByEmail(email: String): Boolean = repository.existsByEmail(email)

    override fun findByUsername(username: String?): Optional<User> = repository.findByUsername(username)

    private fun getCurrentUserEmail(): String {
        val user = SecurityContextHolder.getContext().authentication.principal as JwtUser
        return user.username
    }
}