package com.jmfg.training.kafka.producer.controller

import com.jmfg.training.kafka.core.model.product.{ProductCreateRequest, ProductCreatedEvent}
import com.jmfg.training.kafka.core.publisher.ProductPublisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{PostMapping, RequestBody, RestController}

@RestController("product")
class ProductController @Autowired (productPublisher: ProductPublisher) {

  @PostMapping(path = Array("create"))
  def create(
      @RequestBody request: ProductCreateRequest
  ): ProductCreatedEvent = {
    productPublisher.sendCreate(request)
  }
}
