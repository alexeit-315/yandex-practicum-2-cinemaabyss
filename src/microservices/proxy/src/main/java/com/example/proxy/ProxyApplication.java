package com.example.proxy;

import com.example.proxy.filters.ResponseFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;

@SpringBootApplication
public class ProxyApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder
				.setConnectTimeout(Duration.ofSeconds(5))
				.setReadTimeout(Duration.ofSeconds(5))
				.build();
	}
	@Bean
	public FilterRegistrationBean<ResponseFilter> responseFilter() {
		FilterRegistrationBean<ResponseFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new ResponseFilter());
		registrationBean.addUrlPatterns("/api/movies");
		registrationBean.setOrder(1); // Установите подходящий порядок фильтрации
		return registrationBean;
	}
}