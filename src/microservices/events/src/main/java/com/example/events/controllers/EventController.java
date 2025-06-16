package com.example.events.controllers;

import com.example.events.models.*;
import com.example.events.services.EventProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Map;
import java.util.Collections;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventProducerService eventProducerService;

    @PostMapping("/movie")
    public ResponseEntity<EventResponse> createMovieEvent(@RequestBody MovieEvent movieEvent) {
        log.debug("Info message: Entering sendMovieEvent");
        Event event = new Event();
        event.setId(UUID.randomUUID().toString());
        event.setType("movie");
        event.setTimestamp(LocalDateTime.now());
        event.setPayload(movieEvent);

        eventProducerService.sendMovieEvent(event);

        log.debug("Info message: Entering EventResponse");
        return ResponseEntity.status(HttpStatus.CREATED).body(new EventResponse("success", 0, 0, event));
    }

    @PostMapping("/user")
    public ResponseEntity<EventResponse> createUserEvent(@RequestBody UserEvent userEvent) {
        log.debug("Info message: Entering sendUserEvent");
        Event event = new Event();
        event.setId(UUID.randomUUID().toString());
        event.setType("user");
        event.setTimestamp(LocalDateTime.now());
        event.setPayload(userEvent);

        eventProducerService.sendUserEvent(event);

        log.debug("Info message: Entering EventResponse");
        return ResponseEntity.status(HttpStatus.CREATED).body(new EventResponse("success", 0, 0, event));
    }

    @PostMapping("/payment")
    public ResponseEntity<EventResponse> createPaymentEvent(@RequestBody PaymentEvent paymentEvent) {
        log.debug("Info message: Entering sendPaymentEvent");
        Event event = new Event();
        event.setId(UUID.randomUUID().toString());
        event.setType("payment");
        event.setTimestamp(LocalDateTime.now());
        event.setPayload(paymentEvent);

        eventProducerService.sendPaymentEvent(event);
        log.debug("Info message: Entering EventResponse");

        return ResponseEntity.status(HttpStatus.CREATED).body(new EventResponse("success", 0, 0, event));
    }


    @GetMapping("/health")
    public ResponseEntity<Map<String, Boolean>> healthCheck() {
        log.debug("Info message: Entering healthCheck");
        return ResponseEntity.ok(Collections.singletonMap("status", true));
    }

}