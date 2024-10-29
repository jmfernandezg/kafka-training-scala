package com.jmfg.training.kafka.consumer.service

import com.jmfg.training.kafka.consumer.repository.{
  DepositRequestedEventRepository,
  WithdrawalRequestedEventRepository
}
import com.jmfg.training.kafka.core.model.transfer.{
  DepositRequestedEvent,
  Transfer,
  TransferRequest,
  WithdrawalRequestedEvent
}
import com.jmfg.training.kafka.core.service.TransferService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TransferServiceImpl @Autowired() (
    depositRequestedEventRepository: DepositRequestedEventRepository,
    withdrawalRequestedEventRepository: WithdrawalRequestedEventRepository
) extends TransferService {

  override def handleTransfer(transferRequest: TransferRequest): Transfer = {
    ???
  }

  override def handleDeposit(
      depositRequestEvent: DepositRequestedEvent
  ): Boolean = {
    depositRequestedEventRepository.save(depositRequestEvent)
    true
  }

  override def handleWithdrawal(
      withdrawalRequestEvent: WithdrawalRequestedEvent
  ): Boolean = {
    withdrawalRequestedEventRepository.save(withdrawalRequestEvent)
    true
  }
}
