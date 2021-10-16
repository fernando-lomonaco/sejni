package br.com.lomonaco.sejni.repository

import br.com.lomonaco.sejni.model.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<Account, Long> {
}