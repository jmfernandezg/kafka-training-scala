package com.jmfg.training.kafka.consumer.service

import com.jmfg.training.kafka.consumer.repository.{DepositRequestedEventRepository, TransferRepository, TransferRequestRepository, WithdrawalRequestedEventRepository}
import com.jmfg.training.kafka.core.exceptions.RetryableException
import com.jmfg.training.kafka.core.model.transfer.{DepositRequestedEvent, Transfer, TransferRequest, WithdrawalRequestedEvent}
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
    transferRequestRepository.save(validateTransferRequest(transferRequest))
  }

  override def handleDeposit(
      depositRequestEvent: DepositRequestedEvent
  ): Unit = {
    depositRequestedEventRepository.save(
      validateDepositRequest(depositRequestEvent)
    )
  }

  override def handleWithdrawal(
      withdrawalRequestEvent: WithdrawalRequestedEvent
  ): Unit = {
    withdrawalRequestedEventRepository.save(withdrawalRequestEvent)
    commitTransfer(withdrawalRequestEvent)
  }

  @Retryable(
    retryFor = Array(classOf[RetryableException]),
    maxAttempts = 3,
    backoff = new Backoff(delay = 2000)
  )
  private def validateTransferRequest(
      transferRequest: TransferRequest
  ): TransferRequest = {
    try {
      restTemplate.getForObject(
        s"/validate/transfer/${transferRequest.id}",
        classOf[TransferRequest]
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
  ): DepositRequestedEvent = {
    try {
      restTemplate.getForObject(
        s"/validate/deposit/${event.id}",
        classOf[DepositRequestedEvent]
      )
    } catch {
      case e: HttpStatusCodeException =>
        throw RetryableException("Failed to validate deposit")
    }
  }

  def commitTransfer(
      event: WithdrawalRequestedEvent
  ): Transfer = {
    val transfer = new Transfer()
    transfer.id = event.id
    transfer.withdrawalRequestedEvent = event
    transfer.comment = "Transfer committed"
    transferRepository.save(transfer)
  }
}
