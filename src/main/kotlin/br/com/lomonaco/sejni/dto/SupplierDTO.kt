package br.com.lomonaco.sejni.dto

import br.com.lomonaco.sejni.enums.ServiceType
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class SupplierDTO(
    val id: Long = -1,
    @field:NotBlank(message = "Nome da empresa/instituição é obrigatório")
    val name: String,
    @Pattern(
        regexp = "^(BUFFET|CHURCH|GUESTS)$",
        message = "Somente os perfis BUFFET, CHURCH ou GUESTS são aceitos."
    )
    @field:NotBlank(message = "Tipo de serviço é obrigatório")
    val serviceType: ServiceType,
    @field:NotBlank(message = "Nome do do responsável é obrigatório")
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
