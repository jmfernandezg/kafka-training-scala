package com.jmfg.training.kafka.core.model.transfer

import java.util.UUID
import jakarta.persistence._

@Entity
class WithdrawalRequestedEvent {
  @Id
  var id: String = UUID.randomUUID().toString

  @OneToOne(cascade = Array(CascadeType.ALL), fetch = FetchType.LAZY)
  @JoinColumn(name = "deposit_requested_event_id")
  var depositRequestedEvent: DepositRequestedEvent = _
}