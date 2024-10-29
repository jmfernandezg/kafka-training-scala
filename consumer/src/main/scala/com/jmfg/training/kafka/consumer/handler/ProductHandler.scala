package com.jmfg.training.kafka.consumer.handler

import com.jmfg.training.kafka.core.model.product.ProductCreatedEvent
import com.jmfg.training.kafka.core.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.{KafkaHandler, KafkaListener}
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
@KafkaListener(topics = Array("product-created-event-topic"))
class ProductHandler @Autowired() (productService: ProductService) {
  @KafkaHandler
  def handle(@Payload event: ProductCreatedEvent): Unit = {
    productService.handleEvent(event)
  }
}
