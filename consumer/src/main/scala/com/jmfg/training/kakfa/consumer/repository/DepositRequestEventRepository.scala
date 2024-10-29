package com.jmfg.training.kakfa.consumer.repository


import com.jmfg.training.kafka.core.model.transfer.DepositRequestedEvent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait DepositRequestedEventRepository extends JpaRepository[DepositRequestedEvent, Long] {
  def existsById(id: Long): Boolean
}