package com.example.events.models;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Event {
    private String id;
    private String type;
    private LocalDateTime timestamp;
    private Object payload;
}