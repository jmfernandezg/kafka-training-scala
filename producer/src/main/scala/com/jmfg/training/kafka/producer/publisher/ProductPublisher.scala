package com.jmfg.training.kafka.producer.publisher

import org.springframework.stereotype.Service

@Service
class ProductPublisher {
  def publishProduct(id: String): Unit = {
    println(s"Publishing product with ID: $id")
  }
}