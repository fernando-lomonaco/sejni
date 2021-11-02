package br.com.lomonaco.sejni.mapper

import br.com.lomonaco.sejni.dto.UserDTO
import br.com.lomonaco.sejni.enums.RoleType
import br.com.lomonaco.sejni.model.User
import org.springframework.stereotype.Service

@Service
class UserMapper : Mapper<UserDTO, User> {
    override fun fromEntity(entity: User): UserDTO =
        UserDTO(
            username = entity.username,
            email = entity.email,
            password = entity.password,
            role = entity.role.value,
        )

    override fun toEntity(domain: UserDTO): User =
        User(
            id = 0,
            username = domain.username,
            email = domain.email,
            password =  domain.password,
            role = RoleType.valueOf(domain.role.uppercase())
        )
}