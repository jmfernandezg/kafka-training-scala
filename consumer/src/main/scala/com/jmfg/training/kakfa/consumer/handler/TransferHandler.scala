package com.jmfg.training.kakfa.consumer.handler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.{KafkaHandler, KafkaListener}
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

import com.jmfg.training.kafka.core.model.transfer.{
  Transfer,
  TransferRequest,
  DepositRequestedEvent,
  WithdrawalRequestedEvent
}
import com.jmfg.training.kakfa.consumer.repository.DepositRequestedEventRepository
import com.jmfg.training.kafka.core.service.TransferService

import com.jmfg.training.kafka.core.exceptions.RetryableException

@Component
@KafkaListener(topics = Array("deposit-requested-event-topic", "withdrawal-requested-event-topic"))
class TransferHandler @Autowired() (
    transferService: TransferService,
    DepositRequestedEventRepository: DepositRequestedEventRepository
) {
  @KafkaHandler
  def handleDeposit(@Payload event: DepositRequestedEvent): Unit = {
    processDeposit(event)
    DepositRequestedEventRepository.save(event)
  }

  def processDeposit(event: DepositRequestedEvent): Unit = {
    try {
      val depositId = event.id
      val depositResponse = transferService.deposit(event)
    } catch {
      case e: Exception =>
        throw RetryableException(s"Failed to process event: ${e.getMessage}")
    }
  }
  
  @KafkaHandler
  def handleWithdrawal(@Payload event: WithdrawalRequestedEvent): Unit = {
    processWithdrawal(event)
  }

  def processWithdrawal(event: WithdrawalRequestedEvent): Unit = {
    try {
      transferService.withdrawal(event)
    } catch {
      case e: Exception =>
        throw RetryableException(s"Failed to process withdrawal event: ${e.getMessage}")
    }
  }
}
