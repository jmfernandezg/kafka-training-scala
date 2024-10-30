package com.jmfg.training.kafka.consumer.service

import com.jmfg.training.kafka.consumer.repository.{
  DepositRequestedEventRepository,
  TransferRepository,
  TransferRequestRepository,
  WithdrawalRequestedEventRepository
}
import com.jmfg.training.kafka.core.exceptions.RetryableException
import com.jmfg.training.kafka.core.model.transfer.{
  DepositRequestedEvent,
  Transfer,
  TransferRequest,
  WithdrawalRequestedEvent
}
import com.jmfg.training.kafka.core.service.TransferService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.retry.annotation.{Backoff, Retryable}
import org.springframework.stereotype.Service
import org.springframework.web.client.{HttpStatusCodeException, RestTemplate}

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
    validateTransferRequest(transferRequest)
    transferRequestRepository.save(transferRequest)
  }

  override def handleDeposit(
      depositRequestEvent: DepositRequestedEvent
  ): Unit = {
    validateDepositRequest(depositRequestEvent)
    depositRequestedEventRepository.save(depositRequestEvent)
  }

  override def handleWithdrawal(
      withdrawalRequestEvent: WithdrawalRequestedEvent
  ): Unit = {
    commitTransfer(withdrawalRequestEvent)
    withdrawalRequestedEventRepository.save(withdrawalRequestEvent)
  }

  @Retryable(
    retryFor = Array(classOf[RetryableException]),
    maxAttempts = 3,
    backoff = new Backoff(delay = 2000)
  )
  private def validateTransferRequest(
      transferRequest: TransferRequest
  ): Unit = {
    try {
      restTemplate.postForObject(
        "/validate/transfer",
        transferRequest,
        classOf[Void]
      )
    } catch {
      case e: HttpStatusCodeException =>
        throw RetryableException("Failed to validate transfer")
    }
  }

  @Retryable(
    retryFor = Array(classOf[RetryableException]),
    maxAttempts = 4,
    backoff = new Backoff(delay = 3000)
  )
  private def validateDepositRequest(
      event: DepositRequestedEvent
  ): Unit = {
    try {
      restTemplate.postForObject(
        "/validate/deposit",
        event,
        classOf[Void]
      )
    } catch {
      case e: HttpStatusCodeException =>
        throw RetryableException("Failed to validate deposit")
    }
  }

  def commitTransfer(
      event: WithdrawalRequestedEvent
  ): Unit = {
    val transfer = new Transfer()
    transfer.id = event.id
    transfer.withdrawalRequestedEvent = event
    transfer.comment = "Transfer committed"
    transferRepository.save(transfer)
  }
}
