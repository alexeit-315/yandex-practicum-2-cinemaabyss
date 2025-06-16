package com.example.events.services;

import com.example.events.models.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventConsumerService {

    @KafkaListener(topics = "movie-events", groupId = "events-group")
    public void consumeMovieEvent(Event event) {
        log.info("Consumed Movie Event: {}", event);
    }

    @KafkaListener(topics = "user-events", groupId = "events-group")
    public void consumeUserEvent(Event event) {
        log.info("Consumed User Event: {}", event);
    }

    @KafkaListener(topics = "payment-events", groupId = "events-group")
    public void consumePaymentEvent(Event event) {
        log.info("Consumed Payment Event: {}", event);
    }
}