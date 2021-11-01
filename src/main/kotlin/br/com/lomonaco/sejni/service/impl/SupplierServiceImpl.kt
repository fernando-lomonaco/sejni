package br.com.lomonaco.sejni.service.impl

import br.com.lomonaco.sejni.dto.SupplierDTO
import br.com.lomonaco.sejni.mapper.SupplierMapper
import br.com.lomonaco.sejni.model.Supplier
import br.com.lomonaco.sejni.repository.SupplierRepository
import br.com.lomonaco.sejni.service.SupplierService
import org.springframework.stereotype.Service
import java.util.*

@Service
class SupplierServiceImpl(
    private val repository: SupplierRepository,
    private val supplierMapper: SupplierMapper
) :
    SupplierService {
    override fun getSuppliers(): Collection<Supplier> = repository.findAll()

    override fun getSupplier(id: Long): Optional<Supplier> = repository.findById(id)
    override fun create(supplierDTO: SupplierDTO): SupplierDTO {

        if(supplierDTO.id != -1L)
            throw IllegalArgumentException("Id must be null or -1.")

        val supplier = supplierMapper.toEntity(supplierDTO)
        repository.save(supplier)
        return supplierMapper.fromEntity(supplier)

    }


}