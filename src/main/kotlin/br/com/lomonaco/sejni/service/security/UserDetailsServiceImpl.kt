package br.com.lomonaco.sejni.service.security

import br.com.lomonaco.sejni.model.security.JwtUser
import br.com.lomonaco.sejni.service.UserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val userService: UserService) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val user = userService.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("$username does not exist or is invalid") }
        return JwtUser.create(user)
    }
}