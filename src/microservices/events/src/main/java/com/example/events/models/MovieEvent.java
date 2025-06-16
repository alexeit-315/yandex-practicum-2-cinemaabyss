package com.example.events.models;

import lombok.Data;

@Data
public class MovieEvent {
    private Integer movieId;
    private String title;
    private String action;
    private Integer userId;
    private Double rating;
    private String[] genres;
    private String description;
}