package com.jmfg.training.kafka.consumer.config

import com.jmfg.training.kafka.core.model.product.ProductCreatedEvent
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerConfig.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.kafka.core.{
  DefaultKafkaProducerFactory,
  KafkaTemplate,
  ProducerFactory
}

import java.util

@Configuration
class ProducerConfiguration {
  @Value("${spring.kafka.producer.bootstrap-servers}")
  private var bootstrapServers: String = _

  @Value("${spring.kafka.producer.value-serializer}")
  private var valueSerializer: String = _

  @Value("${spring.kafka.producer.key-serializer}")
  private var keySerializer: String = _

  @Bean
  def producerFactory(): ProducerFactory[String, ProductCreatedEvent] = {
    val props = new util.HashMap[String, Any]()
    props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    props.put(
      KEY_SERIALIZER_CLASS_CONFIG,
      Class.forName(keySerializer)
    )
    props.put(
      VALUE_SERIALIZER_CLASS_CONFIG,
      Class.forName(valueSerializer)
    )
    new DefaultKafkaProducerFactory[String, ProductCreatedEvent](props)
  }

  @Bean
  def kafkaTemplate(): KafkaTemplate[String, ProductCreatedEvent] = {
    new KafkaTemplate(producerFactory())
  }

}
