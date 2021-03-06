package br.com.lomonaco.sejni.controller

import br.com.lomonaco.sejni.configuration.JwtTokenUtil
import br.com.lomonaco.sejni.dto.LoginDTO
import br.com.lomonaco.sejni.dto.UserDTO
import br.com.lomonaco.sejni.dto.response.JwtResponseDTO
import br.com.lomonaco.sejni.dto.response.Response
import br.com.lomonaco.sejni.service.UserService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
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
    private val userDetailsService: UserDetailsService,
) {

    @Autowired
    private lateinit var jwtTokenUtil: JwtTokenUtil

    @PostMapping("login")
    fun login(@Valid @RequestBody loginDTO: LoginDTO): ResponseEntity<Response> {
        val response = Response()

        val authentication =
            UsernamePasswordAuthenticationToken(loginDTO.username, loginDTO.password)

        SecurityContextHolder.getContext().authentication = authentication
        val userDetails = userDetailsService.loadUserByUsername(loginDTO.username)

        val jwt = jwtTokenUtil.getToken(userDetails)
        response.data = JwtResponseDTO.create(jwt, userDetails.username)

        return ResponseEntity.ok(response)
    }

    @PostMapping("signup")
    fun signup(@Valid @RequestBody user: UserDTO): ResponseEntity<Response> {

        val response = Response()

        val existsUsername = service.existsByUsername(user.username)
        val existsEmail = service.existsByEmail(user.email)

        if (existsUsername) {
            response.addMsgToResponse("Aviso, usuario j?? existe")
            return ResponseEntity.badRequest().body(response)
        } else if (existsEmail) {
            response.addMsgToResponse("Aviso, e-mail j?? existe")
            return ResponseEntity.badRequest().body(response)
        }

        val userCreated = service.create(user)
        response.data = userCreated
        return ResponseEntity.created(URI("")).body(response)
    }
}