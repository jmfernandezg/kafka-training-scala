package com.jmfg.training.kafka.consumer.handler

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import com.jmfg.training.kafka.core.exceptions.NonRetryableException
import com.jmfg.training.kafka.core.model.product.ProductCreatedEvent
import com.jmfg.training.kafka.core.repository.ProductCreatedEventRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.{KafkaHandler, KafkaListener}
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.RestClientException
import com.jmfg.training.kafka.core.exceptions.RetryableException
import com.jmfg.training.kafka.core.model.product.Product
import com.jmfg.training.kafka.core.model.product.ProductCreateRequest
import com.jmfg.training.kafka.core.service.ProductService

@Component
@KafkaListener(topics = Array("product-created-event-topic"))
class ProductHandler @Autowired() (
    restTemplate: RestTemplate,
    productService: ProductService,
) {
  @KafkaHandler
  def handle(@Payload event: ProductCreatedEvent): Unit = {
    if (productCreatedEventRepository.existsById(event.id)) {
      throw NonRetryableException(
        s"Event with ID ${event.id} is already processed."
      )
    } else {
      processEvent(event)
      productCreatedEventRepository.save(event)
    }
  }

  def processEvent(event: ProductCreatedEvent): Unit = {
    try {
      val productId = event.productCreateRequest.product.id
      val productResponse = productService.createProduct(event)
      val productResponse =
        restTemplate.getForObject(s"/products/$productId", classOf[Product])
      event.productCreateRequest.product = productResponse
    } catch {
      case e: RestClientException =>
        throw RetryableException(
          s"Failed to fetch product with ID ${event.productCreateRequest.product.id}: ${e.getMessage}"
        )
    }
  }
}
