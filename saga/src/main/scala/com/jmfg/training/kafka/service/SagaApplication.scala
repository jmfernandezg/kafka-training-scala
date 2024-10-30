package com.jmfg.training.kafka.service

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SagaApplication

object SagaApplication extends App {
  SpringApplication.run(classOf[SagaApplication], args: _*)
}
