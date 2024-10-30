package com.jmfg.training.kafka.core.publisher

import com.jmfg.training.kafka.core.model.transfer.{
  DepositRequestedEvent,
  Transfer,
  TransferRequest,
  WithdrawalRequestedEvent
}

trait TransferPublisher {
  def sendRequest(request: TransferRequest): DepositRequestedEvent
  def sendDeposit(event: DepositRequestedEvent): WithdrawalRequestedEvent
  def sendWithdrawal(event: WithdrawalRequestedEvent): Transfer
}
