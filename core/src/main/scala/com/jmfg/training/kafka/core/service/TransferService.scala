package com.jmfg.training.kafka.core.service

import com.jmfg.training.kafka.core.model.transfer.{DepositRequestedEvent, Transfer, TransferRequest, WithdrawalRequestedEvent}

trait TransferService {
  def handleTransfer(transferRequest: TransferRequest): Unit

  def handleDeposit(depositRequestEvent: DepositRequestedEvent): Unit

  def handleWithdrawal(
      withdrawalRequestEvent: WithdrawalRequestedEvent
  ): Unit

  def commitTransfer(withdrawalRequestEvent: WithdrawalRequestedEvent): Transfer
}
