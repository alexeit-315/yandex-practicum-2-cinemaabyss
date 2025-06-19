package com.example.proxy.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Инициализация не требуется
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        CustomResponseWrapper responseWrapper = new CustomResponseWrapper(httpResponse);

        chain.doFilter(request, responseWrapper);

        // Меняем статус только для успешных POST запросов к /api/movies
        if ("POST".equalsIgnoreCase(httpRequest.getMethod()) &&
                httpRequest.getRequestURI().startsWith("/api/movies") &&
                responseWrapper.getStatus() == HttpServletResponse.SC_OK) {
            httpResponse.setStatus(HttpServletResponse.SC_CREATED);
        }
    }

    @Override
    public void destroy() {
        // Очистка не требуется
    }
}