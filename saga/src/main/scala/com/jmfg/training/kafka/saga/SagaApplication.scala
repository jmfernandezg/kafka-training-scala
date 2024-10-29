package com.jmfg.training.kafka.saga

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SagaApplication

object SagaApplication extends App {
  SpringApplication.run(classOf[SagaApplication], args: _*)
}
