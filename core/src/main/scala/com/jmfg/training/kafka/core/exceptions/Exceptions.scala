package com.jmfg.training.kafka.core.exceptions

final case class NonRetryableException(message: String)
    extends RuntimeException(message)

final case class RetryableException(message: String)
    extends RuntimeException(message)

final case class TransferException(message: String)
    extends RuntimeException(message)
