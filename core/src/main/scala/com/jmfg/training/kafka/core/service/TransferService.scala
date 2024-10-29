package com.jmfg.training.kafka.core.service

import com.jmfg.training.kafka.core.model.transfer.{Transfer, TransferRequest, DepositRequestedEvent, WithdrawalRequestedEvent}

trait TransferService {
  def handleTransfer(transferRequest: TransferRequest): Transfer

  def handleDeposit(depositRequestEvent: DepositRequestedEvent): Boolean

  def handleWithdrawal(withdrawalRequestEvent: WithdrawalRequestedEvent): Boolean
}
