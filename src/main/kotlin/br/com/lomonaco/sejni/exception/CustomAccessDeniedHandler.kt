package br.com.lomonaco.sejni.exception

import br.com.lomonaco.sejni.dto.response.ExceptionResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CustomAccessDeniedHandler (private val objectMapper: ObjectMapper) : AccessDeniedHandler {


    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {

     //   logger.info("Forbidden: {}", accessDeniedException?.message)

        response?.status = HttpServletResponse.SC_FORBIDDEN
        response?.contentType = MediaType.APPLICATION_JSON_VALUE

        val exceptionResponse = ExceptionResponse.Builder()
            .message(accessDeniedException!!.message!!)
            .code(HttpStatus.FORBIDDEN.value())
            .status(HttpStatus.FORBIDDEN.reasonPhrase)
            .datetime(LocalDateTime.now())
            .details("API Forbidden")
            .build()

        val outputStream = response?.outputStream
        objectMapper.writeValue(outputStream, exceptionResponse)
        outputStream?.flush()
    }

}