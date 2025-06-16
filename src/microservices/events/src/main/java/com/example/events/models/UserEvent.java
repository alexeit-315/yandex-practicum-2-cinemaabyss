package com.example.events.models;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserEvent {
    private Integer userId;
    private String username;
    private String email;
    private String action;
    private LocalDateTime timestamp;
}