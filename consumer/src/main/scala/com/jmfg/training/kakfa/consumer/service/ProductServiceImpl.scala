package com.jmfg.training.kakfa.consumer.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.RestClientException

import com.jmfg.training.kafka.core.model.product.{ProductCreateRequest, ProductCreatedEvent}
import com.jmfg.training.kafka.core.repository.ProductCreatedEventRepository
import com.jmfg.training.kafka.core.service.ProductService
import com.jmfg.training.kafka.core.exceptions.RetryableException

import com.jmfg.training.kafka.core.model.product.Product

@Service
class ProductServiceImpl @Autowired() (
    productCreatedEventRepository: ProductCreatedEventRepository,
    restTemplate: RestTemplate
) extends ProductService {

  override def createProduct(productCreateRequest: ProductCreateRequest): ProductCreatedEvent = {
    try {
      val productResponse = restTemplate.postForObject(
        "/products/create",
        productCreateRequest,
        classOf[Product]
      )
      productCreateRequest.product = productResponse
      productCreatedEventRepository.save(productResponse)
      productResponse
    } catch {
      case e: RestClientException =>
        throw RetryableException(
          s"Failed to create product with ID ${productCreateRequest.product.id}: ${e.getMessage}"
        )
    }
  }
}