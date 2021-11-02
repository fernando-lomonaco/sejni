package br.com.lomonaco.sejni.model.security

import br.com.lomonaco.sejni.enums.RoleType
import br.com.lomonaco.sejni.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class JwtUser(
    private val _id: Long? = null,
    private val _username: String,
    private val _email: String?,
    private val _password: String,
    private val _authorities: Collection<GrantedAuthority>
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return _authorities
    }

    override fun isEnabled(): Boolean = true
    override fun getUsername(): String = _username
    override fun isCredentialsNonExpired(): Boolean = true
    override fun getPassword(): String = _password
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true

    companion object {
        fun create(user: User): JwtUser {
            return JwtUser(
                user.id,
                user.username,
                user.email,
                user.password,
                createGrantedAuthorities(user.role)
            )
        }

        private fun createGrantedAuthorities(role: RoleType): List<GrantedAuthority> {
            val authorities: MutableList<GrantedAuthority> = ArrayList()
            authorities.add(SimpleGrantedAuthority(role.toString()))
            return authorities
        }
    }
}