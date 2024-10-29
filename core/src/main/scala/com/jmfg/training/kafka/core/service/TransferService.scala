package com.jmfg.training.kafka.core.service

import com.jmfg.training.kafka.core.model.transfer.{Transfer, TransferRequest, DepositRequestedEvent, WithdrawalRequestedEvent}

trait TransferService {
  def transfer(transferRequest: TransferRequest): Transfer

  def deposit(depositRequestEvent: DepositRequestedEvent): Boolean

  def withdrawal(withdrawalRequestEvent: WithdrawalRequestedEvent): Boolean
}
