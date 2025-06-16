package com.example.events.models;

import lombok.Data;

@Data
public class EventResponse {
    private String status;
    private int code;
    private int errorCode;
    private Event data;

    public EventResponse(String status, int code, int errorCode, Event data) {
        this.status = status;
        this.code = code;
        this.errorCode = errorCode;
        this.data = data;
    }
}