package br.com.lomonaco.sejni.service.security

import br.com.lomonaco.sejni.service.UserService
import br.com.lomonaco.sejni.service.impl.UserDetailsImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val userService: UserService) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val user = userService.findByUsername(username).orElseThrow { UsernameNotFoundException("$username does not exist or is invalid") }

        return UserDetailsImpl(user)
    }
}