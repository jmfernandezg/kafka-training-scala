package com.jmfg.training.kafka.consumer.service

import com.jmfg.training.kafka.consumer.repository.ProductCreatedEventRepository
import com.jmfg.training.kafka.core.exceptions.{
  NonRetryableException,
  RetryableException
}
import com.jmfg.training.kafka.core.model.product.{Product, ProductCreatedEvent}
import com.jmfg.training.kafka.core.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.{RestClientException, RestTemplate}

@Service
class ProductServiceImpl @Autowired() (
    productCreatedEventRepository: ProductCreatedEventRepository,
    restTemplate: RestTemplate
) extends ProductService {

  override def handleEvent(
      productCreatedEvent: ProductCreatedEvent
  ): Unit = {
    if (productCreatedEventRepository.existsById(productCreatedEvent.id)) {
      throw NonRetryableException(
        s"Event with ID ${productCreatedEvent.id} is already processed."
      )
    } else {
      productCreatedEvent.productCreateRequest.product = validate(
        productCreatedEvent
      )
      productCreatedEventRepository.save(productCreatedEvent)
    }
  }

  private def validate(
      productCreatedEvent: ProductCreatedEvent
  ): Product = {
    try {
      restTemplate.getForObject(
        s"/validate/product/${productCreatedEvent.id}",
        classOf[Product]
      )
    } catch {
      case e: RestClientException =>
        throw RetryableException(
          s"Failed to get product with ID ${productCreatedEvent.id}: ${e.getMessage}"
        )
    }
  }
}
