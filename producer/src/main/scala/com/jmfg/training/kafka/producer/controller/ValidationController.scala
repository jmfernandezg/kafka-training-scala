package com.jmfg.training.kafka.producer.controller

import com.jmfg.training.kafka.core.model.product.{Product, ProductCreatedEvent}
import com.jmfg.training.kafka.core.model.transfer.{
  DepositRequestedEvent,
  TransferRequest,
  WithdrawalRequestedEvent
}
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/validate")
class ValidationController {}
