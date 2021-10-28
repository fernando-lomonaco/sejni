package br.com.lomonaco.sejni.controller

import br.com.lomonaco.sejni.configuration.JWTUtil
import br.com.lomonaco.sejni.dto.LoginDTO
import br.com.lomonaco.sejni.dto.response.JwtResponseDTO
import br.com.lomonaco.sejni.dto.response.Response
import br.com.lomonaco.sejni.model.User
import br.com.lomonaco.sejni.service.UserService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@Tag(name = "API Auth", description = "Routes of auth")
@RequestMapping("auth")
class AuthController(
    private val service: UserService,
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
) {

    @Autowired
    private lateinit var jwtUtil: JWTUtil

    @PostMapping("signin")
    fun signin(@Valid @RequestBody loginDTO: LoginDTO): ResponseEntity<Response> {
        val response = Response()

        val authentication =
            UsernamePasswordAuthenticationToken(loginDTO.username, loginDTO.password)

        SecurityContextHolder.getContext().authentication = authentication
        val userDetails = userDetailsService.loadUserByUsername(loginDTO.username)

        val jwt = jwtUtil.generateToken(userDetails.username)
        response.data = JwtResponseDTO.create(jwt, userDetails.username)

        return ResponseEntity.ok(response)
    }

    @PostMapping("signup")
    fun signup(@Valid @RequestBody user: User): ResponseEntity<Response> {

        val response = Response()

        val existsUsername = service.existsByName(user.name)
        val existsEmail = service.existsByEmail(user.email)

        if (existsUsername) {
            response.addMsgToResponse("Aviso, usuario já existe")
            return ResponseEntity.badRequest().body(response)
        } else if (existsEmail) {
            response.addMsgToResponse("Aviso, e-mail já existe")
            return ResponseEntity.badRequest().body(response)
        }

        val userCreated = service.create(user)
        response.data = userCreated
        return ResponseEntity.created(URI("")).body(response)
    }
}