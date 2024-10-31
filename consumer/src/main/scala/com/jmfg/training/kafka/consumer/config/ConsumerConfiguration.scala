package com.jmfg.training.kafka.consumer.config

import org.apache.kafka.clients.consumer.ConsumerConfig.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.ContainerProperties

import java.util

@Configuration
class ConsumerConfiguration {
  @Value("${spring.kafka.consumer.bootstrap-servers}")
  var bootstrapServers: String = _

  @Value("${spring.kafka.consumer.group-id}")
  var groupId: String = _

  @Value("${spring.kafka.consumer.key-deserializer}")
  var keyDeserializer: String = _

  @Value("${spring.kafka.consumer.value-deserializer}")
  var valueDeserializer: String = _

  @Value("${spring.kafka.consumer.isolation-level}")
  var isolationLevel: String = _

  @Value(
    "${spring.kafka.consumer.properties.spring.deserializer.value.delegate.class}"
  )
  var propertiesSpringDeserializerValueDelegateClass: String = _

  @Value(
    "${spring.kafka.consumer.properties.spring.deserializer.key.delegate.class}"
  )
  var propertiesSpringDeserializerKeyDelegateClass: String = _

  @Value("${spring.kafka.consumer.properties.spring.json.trusted.packages}")
  var propertiesSpringJsonTrustedPackages: String = _

  @Bean
  def kafkaListenerContainerFactory()
      : ConcurrentKafkaListenerContainerFactory[String, String] = {
    val factory = new ConcurrentKafkaListenerContainerFactory[String, String]()
    factory.setConsumerFactory(consumerFactory())
    factory
  }

  @Bean
  def consumerFactory(): ConsumerFactory[String, String] = {
    val props = new util.HashMap[String, Object]()
    props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    props.put(GROUP_ID_CONFIG, groupId)
    props.put(KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer)
    props.put(VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer)
    props.put(ISOLATION_LEVEL_CONFIG, isolationLevel)
    props.put(
      "spring.json.trusted.packages",
      propertiesSpringJsonTrustedPackages
    )
    props.put(
      "spring.deserializer.key.delegate.class",
      propertiesSpringDeserializerKeyDelegateClass
    )
    props.put(
      "spring.deserializer.value.delegate.class",
      propertiesSpringDeserializerValueDelegateClass
    )
    new DefaultKafkaConsumerFactory[String, String](props)
  }

}
