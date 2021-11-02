package br.com.lomonaco.sejni.dto

import javax.validation.constraints.*

data class UserDTO(
    @field:NotBlank(message = "Nome do usuário é obrigatório")
    @field:Size(min = 3, max = 20, message = "Nome deve ter entre 3 a 20 caracteres.")
    var username: String,
    @field:Size(max = 20, message = "Email deve ter no máximo 20 caracteres.")
    @field:Email(message = "Email invalido.")
    var email: String,
    @field:NotBlank(message = "Senha é obrigatório")
    var password: String,
    @field:NotNull(message = "Perfil do usuário nao pode ser nulo.")
    @field:Pattern(
        regexp = "^(ROLE_ADMIN|ROLE_USER)$",
        message = "Somente os perfis ROLE_ADMIN ou ROLE_USER são aceitos."
    )
    var role: String
)