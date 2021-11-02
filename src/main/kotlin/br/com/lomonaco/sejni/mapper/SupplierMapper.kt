package br.com.lomonaco.sejni.mapper

import br.com.lomonaco.sejni.dto.SupplierDTO
import br.com.lomonaco.sejni.model.Supplier
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SupplierMapper : Mapper<SupplierDTO, Supplier> {
    override fun fromEntity(entity: Supplier): SupplierDTO =
        SupplierDTO(
            entity.id,
            entity.name,
            entity.responsible,
            entity.phone,
            entity.email
        )


    override fun toEntity(domain: SupplierDTO): Supplier = Supplier(
        domain.id,
        domain.name,
        domain.responsible,
        domain.phone,
        domain.email,
        LocalDateTime.now(),
        LocalDateTime.now(),
    )
}