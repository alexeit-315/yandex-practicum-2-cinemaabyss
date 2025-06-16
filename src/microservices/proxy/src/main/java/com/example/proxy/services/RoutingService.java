package com.example.proxy.services;

import com.example.proxy.clients.MonolithClient;
import com.example.proxy.clients.MoviesServiceClient;
import com.example.proxy.config.FeatureFlagConfig;
import com.example.proxy.models.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoutingService {
    private final FeatureFlagConfig featureFlagConfig;
    private final MonolithClient monolithClient;
    private final MoviesServiceClient moviesServiceClient;
    private final Random random = new Random();

    public List<Movie> getAllMovies() {
        if (shouldRouteToMicroservice()) {
            log.info("Routing to movies microservice");
            return moviesServiceClient.getAllMovies();
        } else {
            log.info("Routing to monolith");
            return monolithClient.getAllMovies();
        }
    }

    public Movie getMovieById(Integer id) {
        if (shouldRouteToMicroservice()) {
            log.info("Routing to movies microservice");
            return moviesServiceClient.getMovieById(id);
        } else {
            log.info("Routing to monolith");
            return monolithClient.getMovieById(id);
        }
    }

    public Movie createMovie(Movie movie) {
        if (shouldRouteToMicroservice()) {
            log.info("Routing to movies microservice");
            return moviesServiceClient.createMovie(movie);
        } else {
            log.info("Routing to monolith");
            return monolithClient.createMovie(movie);
        }
    }

    private boolean shouldRouteToMicroservice() {
        if (!featureFlagConfig.isEnabled()) {
            return false;
        }

        if (!moviesServiceClient.isHealthy()) {
            log.warn("Movies service is unhealthy, falling back to monolith");
            return false;
        }

        return random.nextInt(100) < featureFlagConfig.getTrafficPercentage();
    }
}