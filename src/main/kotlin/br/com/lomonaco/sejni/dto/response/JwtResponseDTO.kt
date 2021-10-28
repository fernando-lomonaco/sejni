package br.com.lomonaco.sejni.dto.response

class JwtResponseDTO(
    var token: String,
    var type: String,
    var username: String,
) {

    companion object {
        fun create(token: String, username: String): JwtResponseDTO = JwtResponseDTO(token, "Bearer", username)
    }
}