package com.jmfg.training.kafka.core.service

import com.jmfg.training.kafka.core.model.product.ProductCreateRequest
import com.jmfg.training.kafka.core.model.product.ProductCreatedEvent

trait ProductService {
  def createProduct(productCreateRequest: ProductCreateRequest): ProductCreatedEvent
}