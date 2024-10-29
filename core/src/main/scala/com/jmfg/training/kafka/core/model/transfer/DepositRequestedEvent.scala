package com.jmfg.training.kafka.core.model.transfer

import jakarta.persistence.*

import java.util.UUID

@Entity
class DepositRequestedEvent {
  @Id
  var id: String = UUID.randomUUID().toString

  @OneToOne(cascade = Array(CascadeType.ALL), fetch = FetchType.LAZY)
  @JoinColumn(name = "transfer_request_id")
  var transferRequest: TransferRequest = _
}
