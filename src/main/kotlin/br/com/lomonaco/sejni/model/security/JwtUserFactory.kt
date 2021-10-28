package br.com.lomonaco.sejni.model.security

import br.com.lomonaco.sejni.enums.RoleType
import br.com.lomonaco.sejni.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class JwtUserFactory {



    private fun createGrantedAuthorities(role: RoleType): List<GrantedAuthority>? {
        val authorities: MutableList<GrantedAuthority> = ArrayList()
        authorities.add(SimpleGrantedAuthority(role.toString()))
        return authorities
    }
}