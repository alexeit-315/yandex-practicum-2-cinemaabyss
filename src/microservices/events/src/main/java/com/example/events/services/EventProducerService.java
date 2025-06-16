package com.example.events.services;

import com.example.events.models.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventProducerService {

    private final KafkaTemplate<String, Event> kafkaTemplate;

    public void sendMovieEvent(Event event) {
        kafkaTemplate.send("movie-events", event);
        log.info("Sent Movie Event: {}", event);
    }

    public void sendUserEvent(Event event) {
        kafkaTemplate.send("user-events", event);
        log.info("Sent User Event: {}", event);
    }

    public void sendPaymentEvent(Event event) {
        kafkaTemplate.send("payment-events", event);
        log.info("Sent Payment Event: {}", event);
    }
}