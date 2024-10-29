package com.jmfg.training.kafka.consumer.repository

import com.jmfg.training.kafka.core.model.product.ProductCreatedEvent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait ProductCreatedEventRepository
    extends JpaRepository[ProductCreatedEvent, String]
