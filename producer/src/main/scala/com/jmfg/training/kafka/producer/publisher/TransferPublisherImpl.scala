package com.jmfg.training.kafka.producer.publisher

import com.jmfg.training.kafka.core.model.transfer.{
  DepositRequestedEvent,
  Transfer,
  TransferRequest,
  WithdrawalRequestedEvent
}
import com.jmfg.training.kafka.core.publisher.TransferPublisher
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.transaction.annotation.Transactional

class TransferPublisherImpl(
    kafkaTemplateDepositMoney: KafkaTemplate[
      String,
      DepositRequestedEvent
    ],
    kafkaTemplateWithdrawMoney: KafkaTemplate[
      String,
      WithdrawalRequestedEvent
    ],
    kafkaTemplateTransferRequest: KafkaTemplate[
      String,
      TransferRequest
    ]
) extends TransferPublisher {

  @Transactional
  override def sendRequest(request: TransferRequest): DepositRequestedEvent =
    ???

  @Transactional
  override def sendDeposit(
      event: DepositRequestedEvent
  ): WithdrawalRequestedEvent = ???

  @Transactional
  override def sendWithdrawal(event: WithdrawalRequestedEvent): Transfer = ???
}
