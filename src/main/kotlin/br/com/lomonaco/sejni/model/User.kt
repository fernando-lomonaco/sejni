package br.com.lomonaco.sejni.model

import br.com.lomonaco.sejni.enums.RoleType
import javax.persistence.*

@Entity(name = "sj_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val username: String = "",
    val email: String = "",
    var password: String = "",
    @Enumerated(EnumType.STRING)
    var role: RoleType
)
