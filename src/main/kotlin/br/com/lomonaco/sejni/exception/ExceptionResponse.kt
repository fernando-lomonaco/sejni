package br.com.lomonaco.sejni.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

class ExceptionResponse private constructor(
    val message: String?,
    val code: Int?,
    val status: String?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy HH:mm:ss")
    val datetime: LocalDateTime?,
    val details: String?,
) {

    data class Builder(
        var message: String? = null,
        var code: Int? = 0,
        var status: String? = null,
        var datetime: LocalDateTime? = null,
        var details: String? = null,
    ) {
        fun message(message: String) = apply { this.message = message }
        fun code(code: Int) = apply { this.code = code }
        fun status(status: String) = apply { this.status = status }
        fun datetime(datetime: LocalDateTime) = apply { this.datetime = datetime }
        fun details(details: String) = apply { this.details = details }
        fun build() = ExceptionResponse(message, code, status, datetime, details)
    }
}