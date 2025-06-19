package com.example.proxy.services;

import com.example.proxy.clients.MonolithClient;
import com.example.proxy.clients.MoviesServiceClient;
import com.example.proxy.config.FeatureFlagConfig;
import com.example.proxy.models.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        return routeRequest(
                () -> moviesServiceClient.getAllMovies(),
                () -> monolithClient.getAllMovies()
        );
    }

    public Movie getMovieById(Integer id) {
        return routeRequest(
                () -> moviesServiceClient.getMovieById(id),
                () -> monolithClient.getMovieById(id)
        );
    }

    public Movie createMovie(Movie movie) {
        Movie result = routeRequest(
                () -> moviesServiceClient.createMovie(movie),
                () -> monolithClient.createMovie(movie)
        );

        // Добавляем лог для отладки
        log.info("Created movie with ID: {}", result.getId());
        return result;
    }

    private <T> T routeRequest(Supplier<T> microserviceCall, Supplier<T> monolithCall) {
        // Сначала проверяем крайние случаи
        if (featureFlagConfig.isFullyOnMonolith()) {
            log.info("Routing 100% to monolith (enabled={}, percentage={})",
                    featureFlagConfig.isEnabled(), featureFlagConfig.getTrafficPercentage());
            return monolithCall.get();
        }

        if (featureFlagConfig.isFullyOnMicroservice()) {
            log.info("Routing 100% to microservice (enabled={}, percentage={})",
                    featureFlagConfig.isEnabled(), featureFlagConfig.getTrafficPercentage());
            return microserviceCall.get();
        }

        // Затем проверяем здоровье микросервиса
        if (!moviesServiceClient.isHealthy()) {
            log.warn("Movies service is unhealthy, falling back to monolith");
            return monolithCall.get();
        }

        // Только если не крайние случаи - делаем процентное распределение
        int percentage = featureFlagConfig.getTrafficPercentage();
        boolean useMicroservice = random.nextInt(100) < percentage;

        log.info("Routing decision: {}% to microservice (selected: {})",
                percentage, useMicroservice ? "microservice" : "monolith");

        return useMicroservice ? microserviceCall.get() : monolithCall.get();
    }
}