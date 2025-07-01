package com.example.events.models;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PaymentEvent {
    private Integer paymentId;
    private Integer userId;
    private Double amount;
    private String status;
    private LocalDateTime timestamp;
    private String methodType;
}