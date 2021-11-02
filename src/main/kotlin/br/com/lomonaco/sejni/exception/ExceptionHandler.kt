package br.com.lomonaco.sejni.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(exception: NoSuchElementException, request: WebRequest): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse(
            message = exception.message,
            code = HttpStatus.NOT_FOUND.value(),
            status = HttpStatus.NOT_FOUND.reasonPhrase,
            datetime = LocalDateTime.now(),
            details = request.getDescription(false)
        )

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(exceptionResponse)
    }


    @ExceptionHandler(IllegalArgumentException::class)
    fun supplierExceptionHandler(exception: Exception, request: WebRequest): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse(
            message = exception.message,
            code = HttpStatus.BAD_REQUEST.value(),
            status = HttpStatus.BAD_REQUEST.reasonPhrase,
            datetime = LocalDateTime.now(),
            details = request.getDescription(false)
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse)

    }

}