package com.jmfg.training.kafka.core.model.transfer

import java.util.UUID
import jakarta.persistence._

@Entity
class Transfer {
  @Id
  var id: String = UUID.randomUUID().toString

  @Column(nullable = true)
  var comment: String = _

  @OneToOne(cascade = Array(CascadeType.ALL), fetch = FetchType.LAZY)
  @JoinColumn(name = "withdrawal_requested_event_id")
  var withdrawalRequestedEvent: WithdrawalRequestedEvent = _
}