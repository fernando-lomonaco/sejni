package br.com.lomonaco.sejni.service.impl

import br.com.lomonaco.sejni.dto.SupplierDTO
import br.com.lomonaco.sejni.mapper.SupplierMapper
import br.com.lomonaco.sejni.model.Account
import br.com.lomonaco.sejni.model.Supplier
import br.com.lomonaco.sejni.repository.SupplierRepository
import br.com.lomonaco.sejni.service.SupplierService
import org.springframework.stereotype.Service
import org.springframework.util.Assert
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

        if (getSuppliers().any { it.name == supplierDTO.name }) {
            throw IllegalArgumentException("Supplier with name ${supplierDTO.name} already exists.")
        }

        val supplier = repository.save(supplierMapper.toEntity(supplierDTO))
        return supplierMapper.fromEntity(supplier)

    }

    override fun delete(supplier: Supplier): Unit = repository.delete(supplier)
    override fun deleteAll() = repository.deleteAll()


}