package com.jmfg.training.kafka.producer.publisher

import com.jmfg.training.kafka.core.model.product.{ProductCreateRequest, ProductCreatedEvent}
import com.jmfg.training.kafka.core.publisher.ProductPublisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.concurrent.CompletableFuture

@Service
class ProductPublisherImpl @Autowired() (
    kafkaTemplateProductCreatedEvent: KafkaTemplate[
      String,
      ProductCreatedEvent
    ]
) extends ProductPublisher {

  @Transactional
  override def sendCreate(
      request: ProductCreateRequest
  ): ProductCreatedEvent = {

    val productCreatedEvent = new ProductCreatedEvent()
    productCreatedEvent.id = request.id
    productCreatedEvent.productCreateRequest = request

    kafkaTemplateProductCreatedEvent.executeInTransaction { operations =>
      operations.sendDefault(request.id, productCreatedEvent)
    }

    productCreatedEvent
  }
}
