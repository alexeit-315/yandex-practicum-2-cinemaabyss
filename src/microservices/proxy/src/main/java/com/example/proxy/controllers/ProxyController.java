package com.example.proxy.controllers;

import com.example.proxy.models.Movie;
import com.example.proxy.services.RoutingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.beans.factory.annotation.Value;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/") // Базовый путь для всех методов контроллера
@RequiredArgsConstructor
@Slf4j
public class ProxyController {
    private final RoutingService routingService;
    private final RestTemplate restTemplate;

    @Value("${proxy.services.monolith.base-url}")
    private String monolithBaseUrl;

    @Value("${proxy.services.movies-service.base-url}")
    private String moviesServiceBaseUrl;

    // Health check вне базового пути /api
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        log.info("Info message: Entering healthCheck");
        log.debug("Entering healthCheck");
        try {
            return ResponseEntity.ok("Proxy service is healthy");
        } finally {
            log.debug("Exiting healthCheck");
        }
    }

    @GetMapping("/api/movies")
    public ResponseEntity<List<Movie>> getAllMovies() {
        log.info("Info message: Entering getAllMovies");
        log.debug("Entering getAllMovies");
        try {
            return ResponseEntity.ok(routingService.getAllMovies());
        } finally {
            log.debug("Exiting getAllMovies");
        }
    }

    @GetMapping("/api/movies/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Integer id) {
        log.debug("Entering getMovieById");
        try {
            return ResponseEntity.ok(routingService.getMovieById(id));
        } finally {
            log.debug("Exiting getMovieById");
        }
    }

    @PostMapping("/api/movies")
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        log.debug("Entering createMovie");
        try {
            Movie createdMovie = routingService.createMovie(movie);
            log.info("=============================");
            log.info("CreateMovie: ", createdMovie);
            log.info("=============================");
            return ResponseEntity
                    .status(HttpStatus.CREATED)  // Явно указываем статус 201
                    .body(createdMovie);
        } finally {
            log.debug("Exiting createMovie");
        }
    }

    // Обработка всех остальных запросов
    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
            RequestMethod.DELETE, RequestMethod.PATCH})
    public ResponseEntity<StreamingResponseBody> redirectToMonolith(HttpServletRequest request)
            throws URISyntaxException, IOException {

        log.debug("Received request: {} {}", request.getMethod(), request.getRequestURI());

        // Удаляем /api из пути, так как монолит уже ожидает его
        String requestUrl = request.getRequestURI().replaceFirst("^/api", "");
        String queryString = request.getQueryString();
        String fullUrl = monolithBaseUrl + requestUrl + (queryString != null ? "?" + queryString : "");

        log.debug("Forwarding to: {}", fullUrl);

        URI uri = new URI(fullUrl);
        HttpHeaders headers = new HttpHeaders();
        request.getHeaderNames().asIterator()
                .forEachRemaining(headerName ->
                        headers.add(headerName, request.getHeader(headerName)));

        HttpMethod method = HttpMethod.valueOf(request.getMethod());

        Object body = null;
        if (!method.equals(HttpMethod.GET)) {
            body = request.getInputStream().readAllBytes();
        }

        RequestEntity<Object> requestEntity = new RequestEntity<>(
                body,
                headers,
                method,
                uri
        );

        ResponseEntity<byte[]> response = restTemplate.exchange(
                requestEntity,
                byte[].class
        );

        HttpHeaders responseHeaders = new HttpHeaders();
        response.getHeaders().forEach((key, values) ->
                values.forEach(value -> responseHeaders.add(key, value)));

        return ResponseEntity
                .status(response.getStatusCode())
                .headers(responseHeaders)
                .body(outputStream -> {
                    outputStream.write(response.getBody());
                    outputStream.flush();
                });
    }
}