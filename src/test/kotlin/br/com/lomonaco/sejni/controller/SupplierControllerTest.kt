package br.com.lomonaco.sejni.controller

import br.com.lomonaco.sejni.dto.SupplierDTO
import br.com.lomonaco.sejni.service.SupplierService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(Lifecycle.PER_CLASS)
internal class SupplierControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val service: SupplierService,
    val objectMapper: ObjectMapper
) {

    private val baseUrl = "/api/suppliers"

    @BeforeEach
    internal fun beforeEach() {
        service.deleteAll()
    }

    @Nested
    @DisplayName("GET /api/suppliers")
    inner class GetSuppliers {

        @Test
        fun `should return all suppliers`() {
            val supplier = SupplierDTO(
                name = "My Supplier",
                responsible = "Any Responsible",
                phone = "Any Phone",
                email = "Any Email",
                createdDate = LocalDateTime.now(),
                updatedDate = LocalDateTime.now()
            )
            service.create(supplier)

            // when / then
            mockMvc.get(baseUrl)
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].name") {
                        isString()
                    }
                    jsonPath("$[0].responsible") {
                        value("Any Responsible")
                    }
                }
        }

    }

    @Nested
    @DisplayName("GET /api/suppliers/{id}")
    inner class GetSupplier {
        @Test
        fun `should return the supplier with the given id `() {
            // given
            val supplier = service.create(
                SupplierDTO(
                    name = "My Supplier",
                    responsible = "Any Responsible",
                    phone = "Any Phone",
                    email = "Any Email",
                    createdDate = LocalDateTime.now(),
                    updatedDate = LocalDateTime.now()
                )
            )

            // when/then
            mockMvc.get("$baseUrl/${supplier.id}")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.name") {
                        value("My Supplier")
                    }
                    jsonPath("$.id") {
                        isNumber()
                    }
                }
        }

        @Test
        fun `should return NOT FOUND if the supplier id does not exist `() {
            // given
            val id = 404L

            // when/then
            mockMvc.get("$baseUrl/$id")
                .andDo { print() }
                .andExpect { status { isNotFound() } }

        }
    }

    @Nested
    @DisplayName("POST /api/suppliers")
    inner class PostNewSupplier {

        @Test
        fun `should add the new supplier`() {
            // given
            val newSupplier = SupplierDTO(
                name = "My Supplier",
                responsible = "Any Responsible",
                phone = "Any Phone",
                email = "Any Email",
                createdDate = LocalDateTime.now(),
                updatedDate = LocalDateTime.now()
            )

            // when
            val performPost = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newSupplier)
            }

            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.data.name") {
                        value("My Supplier")
                    }
                    jsonPath("$.data.phone") {
                        value("Any Phone")
                    }
                    jsonPath("$.data.email") {
                        value("Any Email")
                    }
                }

            Assertions.assertFalse(service.getSuppliers().isEmpty())
        }

        @Test
        fun `should return BAD REQUEST if supplier with given name already exists`() {
            // given
            service.create(
                SupplierDTO(
                    name = "My Supplier",
                    responsible = "Any Responsible",
                    phone = "Any Phone",
                    email = "Any Email",
                    createdDate = LocalDateTime.now(),
                    updatedDate = LocalDateTime.now()
                )
            )
            val invalidNewSupplier = SupplierDTO(
                name = "My Supplier",
                responsible = "Any Responsible1",
                phone = "Any Phone1",
                email = "Any Email1",
                createdDate = LocalDateTime.now(),
                updatedDate = LocalDateTime.now()
            )

            // when
            val performPost = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidNewSupplier)
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
                            value("Supplier with name ${invalidNewSupplier.name} already exists.")
                        }
                    }
                }
        }
    }

    /*@Nested
    @DisplayName("PATCH /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PatchExistingBank {

        @Test
        fun `should update an existing bank`() {
            // given
            val updateBank = Bank("1234", 1.0, 1)

            // when
            val performPatchRequest = mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updateBank)
            }

            // then
            performPatchRequest
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(updateBank))
                    }
                }

            mockMvc.get("$baseUrl/${updateBank.accountNumber}")
                .andExpect { content { json(objectMapper.writeValueAsString(updateBank)) } }
        }

        @Test
        fun `should return NOT FOUND if no bank with given account number exists`() {
            // given
            val invalidBank = Bank("not_found", 1.0, 1)

            // when
            val performPatchRequest = mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }
            // then
            performPatchRequest
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }
    }*/

    @Nested
    @DisplayName("DELETE /api/suppliers/{id}")
    inner class DeleteExistingSupplier {

        @Test
        fun `should delete the supplier with the given id`() {
            // given
            val supplier = service.create(
                SupplierDTO(
                    name = "My Supplier",
                    responsible = "Any Responsible",
                    phone = "Any Phone",
                    email = "Any Email",
                    createdDate = LocalDateTime.now(),
                    updatedDate = LocalDateTime.now()
                )
            )

            // when/then
            mockMvc.delete("$baseUrl/${supplier.id}")
                .andDo { print() }
                .andExpect {
                    status { isNoContent() }
                }

            mockMvc.get("$baseUrl/${supplier.id}")
                .andExpect { status { isNotFound() } }

            val findById = service.getSupplier(supplier.id)
            Assertions.assertFalse(findById.isPresent)
        }

        @Test
        fun `should return NOT FOUND if no supplier with given id number exists`() {
            // given
            val invalidSupplierId = 404L

            // when/then
            mockMvc.delete("$baseUrl/$invalidSupplierId")
                .andDo { print() }
                .andExpect { status { isNotFound() } }

        }

    }

}