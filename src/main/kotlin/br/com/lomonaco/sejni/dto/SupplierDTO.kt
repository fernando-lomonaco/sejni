package br.com.lomonaco.sejni.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class SupplierDTO(
    val id: Long = -1,
    @field:NotBlank(message = "Nome da empresa/instituição é obrigatório")
    @field:Size(min = 3, message = "Nome deve ter mais de 3 caracteres")
    val name: String,
    @field:NotBlank(message = "Nome do responsável é obrigatório")
    val responsible: String,
    @field:NotBlank(message = "Nº de telefone é obrigatório")
    val phone: String,
    @field:NotBlank(message = "E-mail é obrigatório")
    val email: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy HH:mm:ss")
    val createdDate: LocalDateTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy HH:mm:ss")
    val updatedDate: LocalDateTime,
)
