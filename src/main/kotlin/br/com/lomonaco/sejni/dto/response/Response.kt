package br.com.lomonaco.sejni.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus.BAD_REQUEST
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
class Response {

    var data: Any? = null
    var errors: Any? = null

    fun addMsgToResponse(message: String) {
        val exceptionResponse = ExceptionResponse.Builder()
            .message(message)
            .code(BAD_REQUEST.value())
            .status(BAD_REQUEST.reasonPhrase)
            .datetime(LocalDateTime.now())
            .details("Tente novamente")
            .build()

       this.errors = exceptionResponse
    }
}