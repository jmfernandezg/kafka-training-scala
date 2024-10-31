package com.jmfg.training.kafka.consumer.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.DefaultUriBuilderFactory

@Configuration
class RestConfiguration {
  @Value("${spring.http.client.base-url}")
  private var baseUrl: String = _

  @Bean
  def restTemplate(): RestTemplate = {
    val restTemplate = new RestTemplate()
    restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUrl))
    restTemplate
  }
}
