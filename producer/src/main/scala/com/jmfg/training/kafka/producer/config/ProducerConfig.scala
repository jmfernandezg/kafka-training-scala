package com.jmfg.training.kafka.producer.config

import com.jmfg.training.kafka.core.model.product.ProductCreatedEvent
import com.jmfg.training.kafka.core.model.transfer.{
  DepositRequestedEvent,
  WithdrawalRequestedEvent
}
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.{KafkaTemplate, ProducerFactory}
import org.springframework.kafka.transaction.KafkaTransactionManager

import java.util.Properties

@Configuration
@ConfigurationProperties(prefix = "spring.kafka.producer")
class ProducerConfig {
  private var bootstrapServers: String = _
  private var keySerializer: String = _
  private var valueSerializer: String = _
  private var acks: String = _
  private var deliveryTimeoutMs: String = _
  private var lingerMs: String = _
  private var requestTimeoutMs: String = _
  private var enableIdempotence: String = _
  private var maxInFlightRequestsPerConnection: String = _
  private var transactionIdPrefix: String = _
  private var productCreatedEventsTopicName: String = _
  private var depositMoneyTopicName: String = _
  private var withdrawMoneyTopicName: String = _
  private var transferRequestTopicName: String = _

  @Bean
  def kafkaProducer(): KafkaProducer[String, String] = {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer)
    props.put(ProducerConfig.ACKS_CONFIG, acks)
    props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeoutMs)
    props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs)
    props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeoutMs)
    props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, enableIdempotence)
    props.put(
      ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,
      maxInFlightRequestsPerConnection
    )
    props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, transactionIdPrefix)

    new KafkaProducer[String, String](props)
  }

  @Bean
  def kafkaTransactionManager(
      producerFactory: ProducerFactory[String, String]
  ): KafkaTransactionManager[String, String] = {
    new KafkaTransactionManager[String, String](producerFactory)
  }

  @Bean
  def kafkaTemplateProductCreatedEvent(
      producerFactory: ProducerFactory[String, ProductCreatedEvent]
  ): KafkaTemplate[String, ProductCreatedEvent] = {
    val kafkaTemplate =
      new KafkaTemplate[String, ProductCreatedEvent](producerFactory)
    kafkaTemplate.setDefaultTopic(productCreatedEventsTopicName)
    kafkaTemplate
  }

  @Bean
  def kafkaTemplateDepositMoney(
      producerFactory: ProducerFactory[String, DepositRequestedEvent]
  ): KafkaTemplate[String, DepositRequestedEvent] = {
    val kafkaTemplate =
      new KafkaTemplate[String, DepositRequestedEvent](producerFactory)
    kafkaTemplate.setDefaultTopic(depositMoneyTopicName)
    kafkaTemplate
  }

  @Bean
  def kafkaTemplateWithdrawMoney(
      producerFactory: ProducerFactory[String, WithdrawalRequestedEvent]
  ): KafkaTemplate[String, WithdrawalRequestedEvent] = {
    val kafkaTemplate =
      new KafkaTemplate[String, WithdrawalRequestedEvent](producerFactory)
    kafkaTemplate.setDefaultTopic(withdrawMoneyTopicName)
    kafkaTemplate
  }

  @Bean
  def withdrawMoneyTopic(): NewTopic = {
    TopicBuilder
      .name(withdrawMoneyTopicName)
      .partitions(3)
      .replicas(3)
      .config("min.insync.replicas", "2")
      .build()
  }

  @Bean
  def depositMoneyTopic(): NewTopic = {
    TopicBuilder
      .name(depositMoneyTopicName)
      .partitions(3)
      .replicas(3)
      .config("min.insync.replicas", "2")
      .build()
  }

  @Bean
  def transferRequestTopic(): NewTopic = {
    TopicBuilder
      .name(transferRequestTopicName)
      .partitions(3)
      .replicas(3)
      .config("min.insync.replicas", "2")
      .build()
  }

  @Bean
  def productCreatedEventsTopic(): NewTopic = {
    TopicBuilder
      .name(productCreatedEventsTopicName)
      .partitions(3)
      .replicas(3)
      .config("min.insync.replicas", "2")
      .build()
  }

}
