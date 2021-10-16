package br.com.lomonaco.sejni.datasource.network

import br.com.lomonaco.sejni.datasource.BankDataSource
import br.com.lomonaco.sejni.datasource.network.dto.Data
import br.com.lomonaco.sejni.model.Bank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import java.io.IOException

@Repository("network")
class NetworkDataSource(
    @Autowired private val restTemplate: RestTemplate
) : BankDataSource {

    override fun retrieveBanks(): Collection<Bank> {
        val response: ResponseEntity<Data> =
            restTemplate.getForEntity("https://api.imgflip.com/get_memes")

        return response.body?.data?.memes ?: throw IOException("Could not fetch beers from the network")
    }

    override fun retrieveBank(accountNumber: String): Bank {
        TODO("Not yet implemented")
    }

    override fun createBank(bank: Bank): Bank {
        TODO("Not yet implemented")
    }

    override fun updateBank(bank: Bank): Bank {
        TODO("Not yet implemented")
    }

    override fun deleteBank(accountNumber: String) {
        TODO("Not yet implemented")
    }
}