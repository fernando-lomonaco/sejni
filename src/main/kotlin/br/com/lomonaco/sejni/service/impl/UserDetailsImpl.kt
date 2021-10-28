package br.com.lomonaco.sejni.service.impl

import br.com.lomonaco.sejni.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(private val user: User) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }
    override fun isEnabled(): Boolean = true
    override fun getUsername(): String = user.name
    override fun isCredentialsNonExpired(): Boolean = true
    override fun getPassword(): String = user.password
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
}