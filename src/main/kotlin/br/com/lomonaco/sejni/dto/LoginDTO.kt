package br.com.lomonaco.sejni.dto;

import javax.validation.constraints.NotBlank

data class LoginDTO (
    @field:NotBlank(message = "Nome do usuário é obrigatório")
    var username: String = "",
    @field:NotBlank(message = "Senha é obrigatório")
    var password: String = ""
)
