package com.jmfg.training.kafka.core.model.transfer

import java.util.UUID
import jakarta.persistence._

@Entity
class DepositRequestedEvent {
  @Id
  var id: String = UUID.randomUUID().toString

  @OneToOne(cascade = Array(CascadeType.ALL), fetch = FetchType.LAZY)
  @JoinColumn(name = "transfer_request_id")
  var transferRequest: TransferRequest = _
}