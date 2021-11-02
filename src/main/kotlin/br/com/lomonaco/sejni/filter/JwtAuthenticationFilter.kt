package br.com.lomonaco.sejni.filter

import br.com.lomonaco.sejni.configuration.JwtTokenUtil
import br.com.lomonaco.sejni.model.Credentials
import br.com.lomonaco.sejni.model.security.JwtUser
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter : UsernamePasswordAuthenticationFilter {

    private var jwtTokenUtil: JwtTokenUtil

    constructor(authenticationManager: AuthenticationManager, jwtTokenUtil: JwtTokenUtil) : super() {
        this.authenticationManager = authenticationManager
        this.jwtTokenUtil = jwtTokenUtil
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication? {
        try {
            val (email, password) = ObjectMapper().readValue(request.inputStream, Credentials::class.java)

            val token = UsernamePasswordAuthenticationToken(email, password)

            return authenticationManager.authenticate(token)
        } catch (e: Exception) {
            throw UsernameNotFoundException("User not found!")
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        chain: FilterChain?,
        authResult: Authentication
    ) {
        val username = (authResult.principal as JwtUser).username
        val token = jwtTokenUtil.getToken(username)
        response.addHeader("Authorization", "Bearer $token")
    }
}