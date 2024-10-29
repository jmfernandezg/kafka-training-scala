package com.jmfg.training.kafka.consumer.repository

import com.jmfg.training.kafka.core.model.transfer.{DepositRequestedEvent, Transfer, TransferRequest, WithdrawalRequestedEvent}
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait WithdrawalRequestedEventRepository
    extends JpaRepository[WithdrawalRequestedEvent, String]

@Repository
trait DepositRequestedEventRepository
    extends JpaRepository[DepositRequestedEvent, String]

@Repository
trait TransferRepository extends JpaRepository[Transfer, String]

@Repository
trait TransferRequestRepository extends JpaRepository[TransferRequest, String]
