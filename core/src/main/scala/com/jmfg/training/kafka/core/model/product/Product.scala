package com.jmfg.training.kafka.core.model.product

import jakarta.persistence.*

import java.util.UUID

@Entity
class Product {
  @Id
  var id: String = UUID.randomUUID().toString

  @Column(nullable = false)
  var name: String = _

  @Column(nullable = false)
  var description: String = _

  @Column(nullable = false)
  var price: Double = _

  @Column(nullable = false)
  var quantity: Int = _

  @OneToOne(cascade = Array(CascadeType.ALL), fetch = FetchType.LAZY)
  @JoinColumn(name = "product_create_request_id")
  var productCreateRequest: ProductCreateRequest = _
}
