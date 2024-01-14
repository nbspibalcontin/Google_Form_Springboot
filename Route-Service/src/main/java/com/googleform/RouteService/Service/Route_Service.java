package com.googleform.RouteService.Service;

import com.googleform.RouteService.Dto.FormDto;
import com.googleform.RouteService.Dto.FormWithResponseDto;
import com.googleform.RouteService.Exception.FormNotFoundException;
import com.googleform.RouteService.Exception.WebClientException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class Route_Service {
    private final WebClient.Builder webClientBuilder;
    String FORM_URL = "http://localhost:8081/api/form";

    public Route_Service(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    //Get the Questions
    @CircuitBreaker(name = "getQuestions", fallbackMethod = "fallbackGetQuestions")
    public List<FormDto> getQuestions(String code) {
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(FORM_URL + "/code/{code}", code)
                    .retrieve()
                    .bodyToFlux(FormDto.class) // Use bodyToFlux for handling arrays
                    .collectList() // Collect the array elements into a List
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new FormNotFoundException(e.getResponseBodyAsString());
        }
    }

    // Fallback method for the circuit breaker
    private List<FormDto> fallbackGetQuestions(String code, Exception e) {
        // Provide a custom fallback response message
        log.error("Circuit breaker triggered for getQuestions with code {}", code);
        return Collections.emptyList(); // or return a default list or null
    }

    //TODO create validation in response html

    //ADMIN

    //Display the question and response
    public List<FormWithResponseDto> displayQuestionAndResponse(String code) {
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(FORM_URL + "/questionAndResponse/{code}", code)
                    .retrieve()
                    .bodyToFlux(FormWithResponseDto.class)
                    .collectList()
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new WebClientException(e.getResponseBodyAsString());
        }
    }
}
