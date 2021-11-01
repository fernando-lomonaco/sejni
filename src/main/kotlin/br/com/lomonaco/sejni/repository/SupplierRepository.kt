package br.com.lomonaco.sejni.repository

import br.com.lomonaco.sejni.model.Supplier
import org.springframework.data.jpa.repository.JpaRepository

interface SupplierRepository : JpaRepository<Supplier, Long>
