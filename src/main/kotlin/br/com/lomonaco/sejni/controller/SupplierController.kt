package br.com.lomonaco.sejni.controller

import br.com.lomonaco.sejni.dto.SupplierDTO
import br.com.lomonaco.sejni.dto.response.Response
import br.com.lomonaco.sejni.model.Account
import br.com.lomonaco.sejni.model.Supplier
import br.com.lomonaco.sejni.service.SupplierService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@Tag(name = "API supplier", description = "Route of supplies")
@RequestMapping("api/suppliers")
class SupplierController(private val service: SupplierService) {

    @GetMapping
    fun getBanks(): Collection<Supplier> = service.getSuppliers()

    @GetMapping("/{id}")
    fun getBank(@PathVariable id: Long): ResponseEntity<Supplier> =
        service.getSupplier(id).map {
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())

    @Operation(summary = "Create a supplier")
    @ApiResponse(responseCode = "201", description = "Case the supplier has been created")
    @PostMapping
    fun addSupplier(@Valid @RequestBody supplierDTO: SupplierDTO): ResponseEntity<Response> {
        val response = Response()
        val supplierCreated = service.create(supplierDTO)
        response.data = supplierCreated
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    /*  @GetMapping("/{accountNumber}")
      fun getBank(@PathVariable accountNumber: String) = service.getSupplier(accountNumber)

      @PostMapping
      @ResponseStatus(HttpStatus.CREATED)
      fun addBank(@RequestBody bank: Bank): Bank = service.addBank(bank)

      @PatchMapping
      fun updateBank(@RequestBody  bank: Bank): Bank = service.updateBank(bank)
*/
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> =
        service.getSupplier(id).map {
            service.delete(it)
            ResponseEntity.noContent().build<Unit>()
        }.orElse(ResponseEntity.notFound().build())

}