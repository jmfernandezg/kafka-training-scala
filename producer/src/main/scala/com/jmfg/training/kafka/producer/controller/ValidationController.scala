package com.jmfg.training.kafka.producer.controller

import com.jmfg.training.kafka.core.model.product.{Product, ProductCreatedEvent}
import com.jmfg.training.kafka.core.model.transfer.{
  DepositRequestedEvent,
  TransferRequest,
  WithdrawalRequestedEvent
}
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(path = Array("/validate"))
class ValidationController {

  private def validateId(id: String) = {
    if (id.matches(".*\\d$")) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid ID")
    }
  }

  @PostMapping(path = Array("/product"))
  def validateProduct(
      @RequestBody event: ProductCreatedEvent
  ): Product = {
    validateId(event.id)
    event.productCreateRequest.product
  }

  @PostMapping(path = Array("/transfer"))
  def validateTransfer(
      @RequestBody request: TransferRequest
  ): TransferRequest = {
    validateId(request.id)
    request
  }

  @PostMapping(path = Array("/deposit"))
  def validateDeposit(
      @RequestBody event: DepositRequestedEvent
  ): DepositRequestedEvent = {
    validateId(event.id)
    event
  }

  @PostMapping(path = Array("/withdrawal"))
  def validateWithdrawal(
      @RequestBody event: WithdrawalRequestedEvent
  ): WithdrawalRequestedEvent = {
    validateId(event.id)
    event
  }
}
