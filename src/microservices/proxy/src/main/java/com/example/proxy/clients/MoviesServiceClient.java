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
public class MoviesServiceClient {
    private final RestTemplate restTemplate;

    @Value("${proxy.services.movies-service.base-url}/movies")
    private String moviesEndpoint;

    @Value("${proxy.services.movies-service.base-url}/movies/health")
    private String healthEndpoint;

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

    public boolean isHealthy() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    healthEndpoint, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Health check failed for movies service", e);
            return false;
        }
    }
}