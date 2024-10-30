package com.jmfg.training.kafka.core.publisher

import com.jmfg.training.kafka.core.model.product.{
  ProductCreateRequest,
  ProductCreatedEvent
}

trait ProductPublisher {
  def sendCreate(
      request: ProductCreateRequest
  ): ProductCreatedEvent
}
