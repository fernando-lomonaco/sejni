package br.com.lomonaco.sejni.model

import br.com.lomonaco.sejni.enums.RoleType
import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity(name = "sj_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    val id: Long = 0,
    val name: String = "",
    val email: String = "",
    @JsonIgnore
    var password: String = "",
    @Enumerated(EnumType.STRING)
    var role: RoleType
)
