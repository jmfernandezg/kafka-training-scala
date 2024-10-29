package com.jmfg.training.kafka.service

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ServiceApplication

object ServiceApplication extends App {
  SpringApplication.run(classOf[ServiceApplication], args: _*)
}