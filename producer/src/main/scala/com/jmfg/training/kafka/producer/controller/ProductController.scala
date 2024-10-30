package com.jmfg.training.kafka.producer.controller

import com.jmfg.training.kafka.core.model.product.{
  Product,
  ProductCreateRequest,
  ProductCreatedEvent
}
import com.jmfg.training.kafka.core.publisher.ProductPublisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{
  PostMapping,
  RequestBody,
  RequestMapping,
  RestController
}

@RestController
@RequestMapping("/products")
class ProductController @Autowired() (productPublisher: ProductPublisher) {
  @PostMapping("/")
  def create(
      @RequestBody request: ProductCreateRequest
  ): ProductCreatedEvent = {
    productPublisher.sendCreate(request)
  }
}
