package br.com.lomonaco.sejni.controller

import br.com.lomonaco.sejni.model.Account
import br.com.lomonaco.sejni.service.AccountService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
internal class AccountControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val service: AccountService,
    val objectMapper: ObjectMapper
) {

    private val baseUrl = "/api/accounts"

    @Nested
    @DisplayName("GET /api/accounts")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetAccounts {
        @Test
        fun `should return all accounts`() {
            val account = Account(name = "MyAccount", document = "123", phone = "12345678")
            service.create(account)

            mockMvc.get(baseUrl)
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$") {
                        isArray()
                    }
                    jsonPath("$[0].id") {
                        isNumber()
                    }
                    jsonPath("$[0].document") {
                        value("123")
                    }
                }

            service.delete(account)
        }

    }

    @Nested
    @DisplayName("GET /api/accounts/{id}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetAccount {

        @Test
        fun `should return the account with the given id number `() {
            // given
            val account = service.create(Account(id = 1234L, name = "MyAccount", document = "123", phone = "12345678"))

            // when/then
            mockMvc.get("$baseUrl/${account.id}")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.name") {
                        value("MyAccount")
                    }
                    jsonPath("$.document") {
                        value("123")
                    }
                }

            service.delete(account)

        }

        @Test
        fun `should return NOT FOUND if the account id does not exist `() {
            // given
            val id = 404L

            // when/then
            mockMvc.get("$baseUrl/$id")
                .andDo { print() }
                .andExpect { status { isNotFound() } }

        }
    }

    @Nested
    @DisplayName("POST /api/accounts")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostNewAccount {

        @Test
        fun `should add the new account`() {
            // given
            val newAccount = Account(name = "MyAccount", document = "123", phone = "12345678")
            service.deleteAll()
            // when
            val performPost = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newAccount)
            }

            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.name") {
                            value(newAccount.name)
                        }
                        jsonPath("$.document") {
                            value(newAccount.document)
                        }
                        jsonPath("$.phone") {
                            value(newAccount.phone)
                        }
                    }
                }
            assertFalse(service.getAccounts().isEmpty())
        }

        @Test
        fun `should return BAD REQUEST if account with given name is less than 5 character`() {
            // given
            val invalidNewAccount = Account(name = "My", document = "123", phone = "12345678")
            service.deleteAll()

            // when
            val performPost = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidNewAccount)
            }
            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.statusCode") {
                            isNumber()
                            value(HttpStatus.BAD_REQUEST.value())
                        }
                        jsonPath("$.message") {
                            isString()
                            value("${invalidNewAccount.name} must have at least 5 characters.")
                        }
                    }
                }
        }

        @Test
        fun `should return BAD REQUEST if account with given document number already exists`() {
            // given
            service.create(Account(name = "MyAccount", document = "123", phone = "12345678"))
            val invalidNewAccount = Account(name = "MyAccount", document = "123", phone = "12345678")

            // when
            val performPost = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidNewAccount)
            }
            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.statusCode") {
                            isNumber()
                            value(HttpStatus.BAD_REQUEST.value())
                        }
                        jsonPath("$.message") {
                            isString()
                            value("Account with document ${invalidNewAccount.document} already exists.")
                        }
                    }
                }
        }
    }

    @Nested
    @DisplayName("PUT /api/accounts")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PutExistingAccount {

        @Test
        fun `should update an existing account`() {
            // given
            val updateAccount = service.create(Account(name = "MyAccount", document = "123", phone = "12345678"))
                .copy(name = "Update")

            // when
            val performPatchRequest = mockMvc.put("$baseUrl/${updateAccount.id}") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updateAccount)
            }

            // then
            performPatchRequest
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.name") {
                            value(updateAccount.name)
                        }
                        jsonPath("$.document") {
                            value(updateAccount.document)
                        }
                        jsonPath("$.phone") {
                            value(updateAccount.phone)
                        }
                    }
                }

            mockMvc.get("$baseUrl/${updateAccount.id}")
                .andExpect { content { json(objectMapper.writeValueAsString(updateAccount)) } }

            val findById = service.getAccount(updateAccount.id!!)
            assertTrue(findById.isPresent)
            assertEquals(updateAccount.name, findById.get().name)

            service.delete(updateAccount)
        }

    }

    @Nested
    @DisplayName("DELETE /api/accounts/{id}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteExistingAccount {

        @Test
        fun `should delete the account with the given id number`() {
            // given
            val account = service.create(Account(id = 1234L, name = "MyAccount", document = "123", phone = "12345678"))

            // when/then
            mockMvc.delete("$baseUrl/${account.id}")
                .andDo { print() }
                .andExpect {
                    status { isNoContent() }
                }

            mockMvc.get("$baseUrl/${account.id}")
                .andExpect { status { isNotFound() } }

            val findById = service.getAccount(account.id!!)
            assertFalse(findById.isPresent)
            service.delete(account)

        }

        @Test
        fun `should return NOT FOUND if no account with given id number exists`() {
            // given
            val invalidAccountNumber = 404L

            // when/then
            mockMvc.delete("$baseUrl/$invalidAccountNumber")
                .andDo { print() }
                .andExpect { status { isNotFound() } }

        }

    }
}