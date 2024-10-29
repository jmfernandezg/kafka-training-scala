package com.jmfg.training.kafka.consumer.handler

import com.jmfg.training.kafka.consumer.repository.DepositRequestedEventRepository
import com.jmfg.training.kafka.core.model.transfer.{
  DepositRequestedEvent,
  TransferRequest,
  WithdrawalRequestedEvent
}
import com.jmfg.training.kafka.core.service.TransferService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.{KafkaHandler, KafkaListener}
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
@KafkaListener(topics =
  Array(
    "deposit-requested-event-topic",
    "withdrawal-requested-event-topic",
    "transfer-requested-event-topic"
  )
)
class TransferHandler @Autowired() (
    transferService: TransferService,
    DepositRequestedEventRepository: DepositRequestedEventRepository
) {

  @KafkaHandler
  def handleTransfer(@Payload event: TransferRequest): Unit = {
    transferService.handleTransfer(event)
  }

  @KafkaHandler
  def handleDeposit(@Payload event: DepositRequestedEvent): Unit = {
    transferService.handleDeposit(event)
  }

  @KafkaHandler
  def handleWithdrawal(@Payload event: WithdrawalRequestedEvent): Unit = {
    transferService.handleWithdrawal(event)
  }
}
