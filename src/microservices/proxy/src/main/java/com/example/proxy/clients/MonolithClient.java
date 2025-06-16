package com.example.proxy.clients;

import com.example.proxy.models.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MonolithClient {
    private final RestTemplate restTemplate;

    @Value("${proxy.services.monolith.base-url}/movies")
    private String moviesEndpoint;

    public List<Movie> getAllMovies() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(
                moviesEndpoint, Movie[].class);
        return Arrays.asList(response.getBody());
    }

    public Movie getMovieById(Integer id) {
        return restTemplate.getForObject(
                moviesEndpoint + "/" + id, Movie.class);
    }

    public Movie createMovie(Movie movie) {
        return restTemplate.postForObject(
                moviesEndpoint, movie, Movie.class);
    }
}