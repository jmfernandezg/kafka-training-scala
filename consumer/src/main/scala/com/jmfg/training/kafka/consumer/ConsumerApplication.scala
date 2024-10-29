package com.jmfg.training.kafka.consumer

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ConsumerApplication

object ConsumerApplication extends App {
  SpringApplication.run(classOf[ConsumerApplication], "--spring.profiles.active=consumer")
}