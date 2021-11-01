package br.com.lomonaco.sejni.filter

import br.com.lomonaco.sejni.configuration.JWTUtil
import br.com.lomonaco.sejni.service.security.UserDetailsServiceImpl
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JWTAuthorizationFilter(
    private val userDetailService: UserDetailsServiceImpl,
    private val jwtUtil: JWTUtil
) : OncePerRequestFilter() {

    /*@Autowired
    lateinit var userDetailService: UserDetailsService

    @Autowired
    lateinit var jwtUtil: JWTUtil*/

    companion object {
        const val AUTH_HEADER = "Authorization"
        const val BEARER_PREFIX = "Bearer"
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authorizationHeader: String? = request.getHeader(AUTH_HEADER)
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            val auth = getAuthentication(authorizationHeader)
            SecurityContextHolder.getContext().authentication = auth
        }
        chain.doFilter(request, response)
    }

    private fun getAuthentication(authorizationHeader: String?): UsernamePasswordAuthenticationToken {
        val token = authorizationHeader?.substring(7) ?: ""
        if (jwtUtil.isTokenValid(token)) {
            val username = jwtUtil.getUserName(token)
            val user: UserDetails = userDetailService.loadUserByUsername(username)
            return UsernamePasswordAuthenticationToken(user, null, user.authorities)
        }
        throw UsernameNotFoundException("Auth invalid!")
    }
}