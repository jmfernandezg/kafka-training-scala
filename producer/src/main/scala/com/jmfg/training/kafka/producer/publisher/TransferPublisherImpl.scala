package com.jmfg.training.kafka.producer.publisher

import com.jmfg.training.kafka.core.model.transfer.{
  DepositRequestedEvent,
  Transfer,
  TransferRequest,
  WithdrawalRequestedEvent
}
import com.jmfg.training.kafka.core.publisher.TransferPublisher
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
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
  override def sendRequest(request: TransferRequest): DepositRequestedEvent = {

    val event = new DepositRequestedEvent()
    event.id = request.id
    event.transferRequest = request

    kafkaTemplateTransferRequest.executeInTransaction { operations =>
      operations.sendDefault(request.id, request)
    }

    event
  }

  @Transactional
  override def sendDeposit(
      event: DepositRequestedEvent
  ): WithdrawalRequestedEvent = {

    val requestedEvent = WithdrawalRequestedEvent()
    requestedEvent.id = event.id
    requestedEvent.depositRequestedEvent = event

    kafkaTemplateDepositMoney.executeInTransaction { operations =>
      operations.sendDefault(requestedEvent.id, event)
    }

    requestedEvent

  }

  @Transactional
  override def sendWithdrawal(event: WithdrawalRequestedEvent): Transfer = {

    val transfer = Transfer()
    transfer.id = event.id
    transfer.withdrawalRequestedEvent = event

    kafkaTemplateWithdrawMoney.executeInTransaction { operations =>
      operations.sendDefault(transfer.id, event)
    }

    transfer
  }
}
