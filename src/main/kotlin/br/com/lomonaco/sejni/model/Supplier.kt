package br.com.lomonaco.sejni.model

import br.com.lomonaco.sejni.enums.ServiceType
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "sj_supplier")
data class Supplier(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    val name: String,
    @Enumerated(EnumType.STRING)
    val serviceType: ServiceType,
    val responsible: String,
    val phone: String,
    val email: String,
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    val createdDate: LocalDateTime,
    @UpdateTimestamp
    val updatedDate: LocalDateTime
)
