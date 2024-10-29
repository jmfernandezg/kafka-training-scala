package com.jmfg.training.kafka.producer.controller


import com.jmfg.training.kafka.producer.publisher.ProductPublisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductController @Autowired()(productPublisher: ProductPublisher) {

  @GetMapping("/{id}")
  def getProductById(@PathVariable id: String): String = {
    productPublisher.publishProduct(id)
    s"Product with ID $id has been published."
  }
}