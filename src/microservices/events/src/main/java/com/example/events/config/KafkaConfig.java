package com.example.events.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic movieEventsTopic() {
        return TopicBuilder.name("movie-events").build();
    }

    @Bean
    public NewTopic userEventsTopic() {
        return TopicBuilder.name("user-events").build();
    }

    @Bean
    public NewTopic paymentEventsTopic() {
        return TopicBuilder.name("payment-events").build();
    }
}