package com.jmfg.training.kafka.core.service

import com.jmfg.training.kafka.core.model.transfer.{DepositRequestedEvent, TransferRequest, WithdrawalRequestedEvent}

trait TransferService {
  def handleTransfer(transferRequest: TransferRequest): Unit

  def handleDeposit(depositRequestEvent: DepositRequestedEvent): Unit

  def handleWithdrawal(
      withdrawalRequestEvent: WithdrawalRequestedEvent
  ): Unit
}
