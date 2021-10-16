package br.com.lomonaco.sejni.service

import br.com.lomonaco.sejni.model.Account
import java.util.*

interface AccountService {
    fun getAccounts(): Collection<Account>
    fun getAccount(id: Long): Optional<Account>
    fun create(account: Account): Account
    fun update(id: Long, account: Account): Optional<Account>?
    fun delete(account: Account)
    fun deleteAll()
}