package com.jmfg.training.kafka.core.model.transfer

import jakarta.persistence.*

import java.util.UUID

@Entity
class TransferRequest {
  @Id
  var id: String = UUID.randomUUID().toString

  @Column(nullable = false)
  var senderId: String = _

  @Column(nullable = false)
  var recipientId: String = _

  @Column(nullable = false)
  var amount: Double = _
}
