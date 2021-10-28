package br.com.lomonaco.sejni.service.impl

import br.com.lomonaco.sejni.model.Account
import br.com.lomonaco.sejni.repository.AccountRepository
import br.com.lomonaco.sejni.service.AccountService
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import java.util.*

@Service
class AccountServiceImpl(private val repository: AccountRepository) : AccountService {

    override fun getAccounts(): Collection<Account> = repository.findAll()
    override fun getAccount(id: Long): Optional<Account> = repository.findById(id)
    override fun create(account: Account): Account {
        if (getAccounts().any { it.document == account.document }) {
            throw IllegalArgumentException("Account with document ${account.document} already exists.")
        }
        Assert.isTrue(account.name.length >=5, "${account.name} must have at least 5 characters.")

        return repository.save(account)
    }

    override fun update(id: Long, account: Account): Optional<Account>? {
        return getAccount(id).map {
            val accountUpdate = it.copy(
                name = account.name,
                document = account.document,
                phone = account.phone
            )
            repository.save(accountUpdate)
        }
    }

    override fun delete(account: Account): Unit = repository.delete(account)
    override fun deleteAll() = repository.deleteAll()
}


