package com.jmfg.training.kafka.consumer

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.kafka.annotation.EnableKafka

@EnableKafka
@EnableJpaRepositories(basePackages =
  Array("com.jmfg.training.kafka.consumer.repository")
)
@EntityScan(basePackages = Array("com.jmfg.training.kafka.core.model"))
@SpringBootApplication
class ConsumerApplication

object ConsumerApplication extends App {
  SpringApplication.run(
    classOf[ConsumerApplication],
    "--spring.profiles.active=consumer"
  )
}
