package com.jmfg.training.kafka.consumer.service

import com.jmfg.training.kafka.consumer.repository.DepositRequestedEventRepository
import com.jmfg.training.kafka.consumer.repository.TransferRepository
import com.jmfg.training.kafka.consumer.repository.TransferRequestRepository
import com.jmfg.training.kafka.consumer.repository.WithdrawalRequestedEventRepository
import com.jmfg.training.kafka.core.exceptions.RetryableException
import com.jmfg.training.kafka.core.model.transfer.DepositRequestedEvent
import com.jmfg.training.kafka.core.model.transfer.Transfer
import com.jmfg.training.kafka.core.model.transfer.TransferRequest
import com.jmfg.training.kafka.core.model.transfer.WithdrawalRequestedEvent
import com.jmfg.training.kafka.core.service.TransferService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate


@Service
class TransferServiceImpl @Autowired() (
    transferRequestRepository: TransferRequestRepository,
    depositRequestedEventRepository: DepositRequestedEventRepository,
    withdrawalRequestedEventRepository: WithdrawalRequestedEventRepository,
    transferRepository: TransferRepository,
    restTemplate: RestTemplate
) extends TransferService {

  private val logger = LoggerFactory.getLogger(classOf[TransferServiceImpl])

  override def handleTransfer(transferRequest: TransferRequest): Unit = {
    val existingTransfer = transferRepository.findById(transferRequest.id)
    if (existingTransfer.isPresent) {
      logger.info(s"Transfer with ID ${transferRequest.id} already exists.")
      return
    }
    invokeDeposit(transferRequest)
    transferRequestRepository.save(transferRequest)
  }

  override def handleDeposit(
      depositRequestEvent: DepositRequestedEvent
  ) = {
    invokeWithdrawal(depositRequestEvent)
    depositRequestedEventRepository.save(depositRequestEvent)
  }

  override def handleWithdrawal(
      withdrawalRequestEvent: WithdrawalRequestedEvent
  ) = {
    invokeTransfer(withdrawalRequestEvent)
    withdrawalRequestedEventRepository.save(withdrawalRequestEvent)
  }
  @Retryable(
    value = Array(classOf[RetryableException]),
    maxAttempts = 3,
    backoff = new Backoff(delay = 2000)
  )
  def invokeDeposit(transferRequest: TransferRequest): Unit = {
    try {
      restTemplate.postForObject(
        "/transfers/deposit",
        transferRequest,
        classOf[Void]
      )
    } catch {
      case e: HttpStatusCodeException =>
        throw new RetryableException("Failed to invoke deposit")
    }
  }

  @Retryable(
    value = Array(classOf[RetryableException]),
    maxAttempts = 3,
    backoff = new Backoff(delay = 2000)
  )
  def invokeWithdrawal(depositRequestEvent: DepositRequestedEvent): Unit = {
    try {
      restTemplate.postForObject(
        "/transfers/withdrawal",
        depositRequestEvent,
        classOf[Void]
      )
    } catch {
      case e: HttpStatusCodeException =>
        throw new RetryableException("Failed to invoke withdrawal")
    }
  }

  @Retryable(
    value = Array(classOf[RetryableException]),
    maxAttempts = 3,
    backoff = new Backoff(delay = 2000)
  )
  def invokeTransfer(
      withdrawalRequestEvent: WithdrawalRequestedEvent
  ): Boolean = {
    try {
      restTemplate.getForObject(
        s"/transfers/validate/${withdrawalRequestEvent.id}",
        classOf[Boolean]
      )
    } catch {
      case e: HttpStatusCodeException =>
        throw new RetryableException("Failed to create transfer")
    }
  }
}
