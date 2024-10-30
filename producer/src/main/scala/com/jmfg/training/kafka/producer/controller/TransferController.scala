package com.jmfg.training.kafka.producer.controller

import com.jmfg.training.kafka.core.model.transfer.{DepositRequestedEvent, Transfer, TransferRequest, WithdrawalRequestedEvent}
import com.jmfg.training.kafka.core.publisher.TransferPublisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{PostMapping, RequestBody, RequestMapping, RestController}

@RestController
@RequestMapping("/transfers")
class TransferController @Autowired (
    transferPublisher: TransferPublisher
) {

  @PostMapping("/request")
  def request(
      @RequestBody transferRequest: TransferRequest
  ): DepositRequestedEvent = {
    transferPublisher.sendRequest(transferRequest)
  }

  @PostMapping("/deposit")
  def deposit(
      @RequestBody depositRequestedEvent: DepositRequestedEvent
  ): WithdrawalRequestedEvent = {
    transferPublisher.sendDeposit(depositRequestedEvent)
  }

  @PostMapping("/withdraw")
  def withdraw(
      @RequestBody withdrawalRequestedEvent: WithdrawalRequestedEvent
  ): Transfer = {
    transferPublisher.sendWithdrawal(withdrawalRequestedEvent)
   
  }
}
