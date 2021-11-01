package br.com.lomonaco.sejni.service

import br.com.lomonaco.sejni.dto.SupplierDTO
import br.com.lomonaco.sejni.model.Supplier
import java.util.*

interface SupplierService {
    fun getSuppliers(): Collection<Supplier>
    fun getSupplier(id: Long): Optional<Supplier>
    fun create(supplierDTO: SupplierDTO): SupplierDTO


}