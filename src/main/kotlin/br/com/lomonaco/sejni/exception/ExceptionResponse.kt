package br.com.lomonaco.sejni.exception

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class ExceptionResponse(
    val message: String?,
    val code: Int,
    val status: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy HH:mm:ss")
    val datetime: LocalDateTime,
    val details: String,
)