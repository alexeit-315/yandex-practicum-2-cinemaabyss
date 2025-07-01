package com.example.proxy.models;

import lombok.Data;
import java.util.List;

@Data
public class Movie {
    private Integer id;
    private String title;
    private String description;
    private List<String> genres;
    private Double rating;
}