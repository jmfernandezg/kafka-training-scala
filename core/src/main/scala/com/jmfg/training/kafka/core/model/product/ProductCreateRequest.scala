package com.jmfg.training.kafka.core.model.product

import java.util.UUID
import java.time.LocalDateTime
import jakarta.persistence._

@Entity
class ProductCreateRequest {
  @Id
  var id: String = UUID.randomUUID().toString

  @Column(nullable = false)
  var createdAt: LocalDateTime = LocalDateTime.now()

  @OneToOne(mappedBy = "productCreateRequest", cascade = Array(CascadeType.ALL), fetch = FetchType.LAZY)
  var product: Product = _

  @OneToOne(cascade = Array(CascadeType.ALL), fetch = FetchType.LAZY)
  @JoinColumn(name = "product_created_event_id")
  var productCreatedEvent: ProductCreatedEvent = _
}