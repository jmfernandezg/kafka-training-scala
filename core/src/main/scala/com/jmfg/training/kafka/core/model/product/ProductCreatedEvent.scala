package com.jmfg.training.kafka.core.model.product

import java.util.UUID
import java.time.LocalDateTime
import jakarta.persistence._

@Entity
class ProductCreatedEvent {
  @Id
  var id: String = UUID.randomUUID().toString

  @Column(nullable = false)
  var createdAt: LocalDateTime = LocalDateTime.now()

  @OneToOne(mappedBy = "productCreatedEvent", cascade = Array(CascadeType.ALL), fetch = FetchType.LAZY)
  var productCreateRequest: ProductCreateRequest = _
}