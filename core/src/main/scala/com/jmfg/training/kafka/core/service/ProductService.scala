package com.jmfg.training.kafka.core.service

import com.jmfg.training.kafka.core.model.product.ProductCreatedEvent

trait ProductService {
  def handleEvent(productCreatedEvent: ProductCreatedEvent): Unit
}
