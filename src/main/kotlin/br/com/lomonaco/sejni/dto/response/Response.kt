package br.com.lomonaco.sejni.dto.response

import br.com.lomonaco.sejni.exception.ExceptionResponse
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus.BAD_REQUEST
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
class Response {

    var data: Any? = null
    var errors: Any? = null

    fun addMsgToResponse(message: String) {
        val exceptionResponse = ExceptionResponse(
            message = message,
            code = BAD_REQUEST.value(),
            status = BAD_REQUEST.reasonPhrase,
            datetime = LocalDateTime.now(),
            details = "Tente Novamente"
        )
        this.errors = exceptionResponse
    }
}