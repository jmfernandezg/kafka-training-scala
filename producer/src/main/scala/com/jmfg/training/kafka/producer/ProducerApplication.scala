package com.jmfg.training.kafka.producer

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ProducerApplication

object ProducerApplication extends App {
  SpringApplication.run(classOf[ProducerApplication], "--spring.profiles.active=producer")
}