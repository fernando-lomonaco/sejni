package br.com.lomonaco.sejni.controller

import br.com.lomonaco.sejni.model.Account
import br.com.lomonaco.sejni.service.AccountService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/accounts")
class AccountController(private val service: AccountService) {

    @GetMapping
    fun getAccounts(): Collection<Account> = service.getAccounts()

    @GetMapping("/{id}")
    fun getAccount(@PathVariable id: Long): ResponseEntity<Account> =
        service.getAccount(id).map {
            ok(it)
        }.orElse(notFound().build())

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody account: Account): Account = service.create(account)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody account: Account): ResponseEntity<Account>? =
        service.update(id, account)?.map {
            ok(it)
        }?.orElse(notFound().build())

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> =
        service.getAccount(id).map {
            service.delete(it)
            noContent().build<Unit>()
        }.orElse(notFound().build())

}