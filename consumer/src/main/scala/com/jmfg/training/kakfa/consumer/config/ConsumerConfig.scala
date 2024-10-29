package com.jmfg.training.kafka.consumer.config

import com.jmfg.training.kafka.core.model.product.ProductCreatedEvent
import org.apache.kafka.clients.consumer.ConsumerConfig.*
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.DefaultUriBuilderFactory

import java.util.HashMap

@Configuration
@EnableKafka
@EnableJpaRepositories(basePackages =
  Array("com.jmfg.training.consumer.repository")
)
@ComponentScan(basePackages = Array("com.jmfg.training.kafka.core.model"))
class ConsumerConfig {

  @Value("${spring.kafka.consumer.bootstrap-servers}")
  private var bootstrapServers: String = _

  @Value("${spring.kafka.consumer.group-id}")
  private var groupId: String = _

  @Value("${spring.kafka.consumer.auto-offset-reset}")
  private var autoOffsetReset: String = _

  @Value("${spring.kafka.consumer.key-deserializer}")
  private var keyDeserializer: String = _

  @Value("${spring.kafka.consumer.value-deserializer}")
  private var valueDeserializer: String = _

  @Value("${spring.kafka.consumer.isolation-level}")
  private var isolationLevel: String = _

  @Value("${spring.http.client.base-url}")
  private var baseUrl: String = _

  @Value("${spring.kafka.producer.value-serializer}")
  private var valueSerializer: String = _

  @Value("${spring.kafka.producer.key-serializer}")
  private var keySerializer: String = _

  @Value(
    "${spring.kafka.consumer.properties.spring.deserializer.value.delegate.class}"
  )
  private var valueDelegateClass: String = _

  @Value(
    "${spring.kafka.consumer.properties.spring.deserializer.key.delegate.class}"
  )
  private var keyDelegateClass: String = _

  @Value("${spring.kafka.consumer.properties.spring.json.trusted.packages}")
  private var trustedPackages: String = _

  @Bean
  def consumerFactory(): ConsumerFactory[String, String] = {
    val props = new HashMap[String, Object]()
    props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    props.put(GROUP_ID_CONFIG, groupId)
    props.put(AUTO_OFFSET_RESET_CONFIG, autoOffsetReset)
    props.put(KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer)
    props.put(VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer)
    props.put(ISOLATION_LEVEL_CONFIG, isolationLevel)
    props.put("spring.json.trusted.packages", trustedPackages)
    props.put("spring.deserializer.key.delegate.class", keyDelegateClass)
    props.put("spring.deserializer.value.delegate.class", valueDelegateClass)
    new DefaultKafkaConsumerFactory[String, String](props)
  }

  @Bean
  def kafkaListenerContainerFactory()
      : ConcurrentKafkaListenerContainerFactory[String, String] = {
    val factory = new ConcurrentKafkaListenerContainerFactory[String, String]()
    factory.setConsumerFactory(consumerFactory())
    factory.getContainerProperties.setAckMode(
      ContainerProperties.AckMode.MANUAL
    )
    factory
  }

  @Bean
  def kafkaTemplate(): KafkaTemplate[String, ProductCreatedEvent] = {
    new KafkaTemplate(producerFactory())
  }

  @Bean
  def producerFactory(): ProducerFactory[String, ProductCreatedEvent] = {
    val props = new HashMap[String, Any]()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    props.put(
      ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
      Class.forName(keySerializer)
    )
    props.put(
      ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
      Class.forName(valueSerializer)
    )
    new DefaultKafkaProducerFactory[String, ProductCreatedEvent](props)
  }

  @Bean
  def restTemplate(): RestTemplate = {
    val restTemplate = new RestTemplate()
    restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUrl))
    restTemplate
  }
}
