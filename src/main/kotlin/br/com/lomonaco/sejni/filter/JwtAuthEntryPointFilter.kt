package br.com.lomonaco.sejni.filter

import br.com.lomonaco.sejni.exception.ExceptionResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthEntryPointFilter(private val objectMapper: ObjectMapper) : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        response?.status = HttpStatus.UNAUTHORIZED.value()
        response?.contentType = MediaType.APPLICATION_JSON_VALUE

        val exceptionResponse = ExceptionResponse(
            message = authException!!.message!!,
            code = HttpStatus.UNAUTHORIZED.value(),
            status = HttpStatus.UNAUTHORIZED.reasonPhrase,
            datetime = LocalDateTime.now(),
            details = "Unauthorized"
        )

        val outputStream = response?.outputStream
        objectMapper.writeValue(outputStream, exceptionResponse)
        outputStream?.flush()
    }
}