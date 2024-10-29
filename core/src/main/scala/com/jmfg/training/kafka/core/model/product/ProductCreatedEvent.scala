package com.jmfg.training.kafka.core.model.product

import jakarta.persistence.*

import java.time.LocalDateTime
import java.util.UUID

@Entity
class ProductCreatedEvent {
  @Id
  var id: String = UUID.randomUUID().toString

  @Column(nullable = false)
  var createdAt: LocalDateTime = LocalDateTime.now()

  @OneToOne(
    mappedBy = "productCreatedEvent",
    cascade = Array(CascadeType.ALL),
    fetch = FetchType.LAZY
  )
  var productCreateRequest: ProductCreateRequest = _
}
